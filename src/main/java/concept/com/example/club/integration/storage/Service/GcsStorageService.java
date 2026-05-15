package concept.com.example.club.integration.storage.Service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
@Service
public class GcsStorageService {

    private final Storage storage; // Interface nativa do Google Cloud injetada automaticamente pelo Spring
    private final String bucketName;

    public GcsStorageService(Storage storage, @Value("${spring.cloud.gcp.storage.bucket}") String bucketName) {
        this.storage = storage;
        this.bucketName = bucketName;
    }

    public String uploadBanner(MultipartFile file) {
        try {
            // 1. Gera um nome único para evitar colisões
            String originalName = file.getOriginalFilename();
            String extension = originalName != null ? originalName.substring(originalName.lastIndexOf(".")) : ".jpg";
            String fileName = "banners/" + UUID.randomUUID().toString() + extension; // Salva numa pasta "banners" no bucket

            // 2. Prepara as informações do arquivo para o GCP
            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .build();

            // 3. Faz o upload dos bytes
            storage.create(blobInfo, file.getBytes());

            // 4. Retorna a URL pública padrão do Google Cloud Storage
            return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);

        } catch (IOException e) {
            throw new RuntimeException("Falha ao fazer upload da imagem para o Google Cloud", e);
        }
    }

}
