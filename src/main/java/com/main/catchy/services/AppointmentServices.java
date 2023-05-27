package com.main.catchy.services;

import com.main.catchy.model.Appointment;
import com.main.catchy.repository.AppointmentRepository;
import com.main.catchy.repository.ContactRepository;
import com.main.catchy.repository.UserRepository;
import com.main.catchy.services.Externe.NotificationServices;
import com.main.catchy.utils.AppointmentBody;
import com.main.catchy.utils.Constant;
import com.main.catchy.utils.NotificationBody;
import com.main.catchy.utils.Responce.Response;
import com.main.catchy.utils.Responce.UARC;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@Component
@AllArgsConstructor
public class AppointmentServices {
    private final ContactRepository cntDao;
    private final UserRepository usrDao;
    private final AppointmentRepository appointmentRepo;
    private final NotificationServices notifServices;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Response<Object> createAppointment(AppointmentBody data, String token) {
        try {
            var mentee = cntDao.finContactBYID(data.getMenteeId());
            var mentor = cntDao.finContactBYID(data.getMentorId());
            var app = Appointment.builder()
                    .creationDate(LocalDateTime.now())
                    .mentee(mentee)
                    .mentor(mentor)
                    .scheduledDate(data.getScheduledDate())	
                    .status(Constant.CREATE_APPOINTMENT)
                    .build();
            appointmentRepo.save(app);
            ///send notification
            var not = NotificationBody.builder()
                    .receiverId(mentor.getUsr().getId())
                    .receiverNtf(mentor.getNotifId())
                    .senderEmail(mentee.getEmail())
                    .senderName(mentee.getFullName())
                    .appointmentId(app.getAppointmentId())
                    .build();
            sendNotificationProcess(Constant.CREATE_APPOINTMENT, not);
            Response<Object> reps = new Response<Object>();
            reps.setStatus("200");
            reps.setResp(new UARC(200), HttpStatus.OK);
            logger.info("AppointmentServices::::::methode=  createAppointment() =====> execute succssefully!");
            return reps;
        } catch (Exception e) {
            Response<Object> reps = new Response<Object>();
            reps.setStatus("403");
            reps.setResp(new UARC(403), HttpStatus.BAD_REQUEST);
            logger.info("AppointmentServices::::::methode=  createAppointment() =====> bad Request");
            return reps;
        }
    }

    public Response<Object> confirmeAppointment(AppointmentBody data, String token) {
        try {
            var mentee = cntDao.finContactBYID(data.getMenteeId());
            var mentor = cntDao.finContactBYID(data.getMentorId());
            var appintment = appointmentRepo.findById(data.getAppointmentId()).orElse(null);
            if (appintment != null) {
                appintment.setStatus(data.getStatus());
                if (data.getStatus().equals("CONFIRMED")) {
                    appintment.setScheduledDate(data.getScheduledDate());
                    appintment.setScheduledtime(data.getScheduledtime());
                }

                appointmentRepo.save(appintment);
                ///send notification
                var not = NotificationBody.builder()
                        .receiverId(mentee.getUsr().getId())
                        .receiverNtf(mentee.getNotifId())
                        .senderEmail(mentor.getEmail())
                        .senderName(mentor.getFullName())
                        .appointmentId(appintment.getAppointmentId())
                        .build();
                sendNotificationProcess(data.getStatus().equals("CONFIRMED") ? Constant.CONFIRME_APPOINTMENT : Constant.REJECT_APPOINTMENT, not);

            }
            Response<Object> reps = new Response<Object>();
            reps.setStatus("200");
            reps.setResp(new UARC(200), HttpStatus.OK);
            logger.info("AppointmentServices::::::methode=  confirmeAppointment() =====> execute succssefully!");
            return reps;
        } catch (Exception e) {
            Response<Object> reps = new Response<Object>();
            reps.setStatus("403");
            reps.setResp(new UARC(403), HttpStatus.BAD_REQUEST);
            logger.info("AppointmentServices::::::methode=  confirmeAppointment() =====> bad Request");
            return reps;
        }
    }

    private void sendNotificationProcess(String type, NotificationBody data) {
        switch (type) {
            case Constant.CREATE_APPOINTMENT:
                data.setMessage(Constant.CREATE_APPOINTMENT_MESSAGE);
                notifServices.sendNotification(data);
                break;
            case Constant.CONFIRME_APPOINTMENT:
                data.setMessage(Constant.CONFIRME_APPOINTMENT_MESSAGE);
                notifServices.sendNotification(data);
                break;
            case Constant.REJECT_APPOINTMENT:
                data.setMessage(Constant.REJECT_APPOINTMENT_MESSAGE);
                notifServices.sendNotification(data);
                break;
            default:
                logger.warn("--------------- no action has ben made ------ for the Appointment with id {} " + data.getAppointmentId());

        }

    }
}
