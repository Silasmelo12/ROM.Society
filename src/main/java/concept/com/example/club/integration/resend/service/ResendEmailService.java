package concept.com.example.club.integration.resend.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

@Service
@Slf4j
public class ResendEmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    private final Resend resend;

    public ResendEmailService(Resend resend) {
        this.resend = resend;
    }

    @Async
    public void sendRegistrationConfirmation(String toEmail, String userName, String eventTitle) {

        String safeName = HtmlUtils.htmlEscape(userName);
        String safeTitle = HtmlUtils.htmlEscape(eventTitle);

        // O HTML amigável do seu e-mail
        String htmlBody = String.format(
                "<h2>Olá, %s!</h2><p>Sua inscrição no evento <strong>%s</strong> foi confirmada com sucesso.</p><p>Prepare-se para uma experiência inesquecível na ROM.Concept.</p>",
                safeName, safeTitle
        );

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("ROM.Concept <onboarding@resend.dev>") // Precisa ser o domínio verificado no Resend
                .to(toEmail)
                .subject("Confirmação de Inscrição VIP - " + eventTitle)
                .html(htmlBody)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            log.info("E-mail de confirmação enviado com sucesso para {}. Resend ID: {}", toEmail, data.getId());
        } catch (ResendException e) {
            // Se o e-mail falhar, nós apenas "logamos" o erro. Não quebramos o sistema.
            log.error("Falha ao enviar e-mail pelo Resend para {}: {}", toEmail, e.getMessage());
        }
    }
}