package uz.cherry.cherry_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.cherry.cherry_server.entity.Attachment;
import uz.cherry.cherry_server.payload.Result;
import uz.cherry.cherry_server.repository.AttachmentRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class AttachmentService {

    @Autowired
    AttachmentRepository attachmentRepository;

    public static final String uploadDirectory = "D:\\Uploaded Files";

    public Result add(MultipartHttpServletRequest request) throws IOException {

        Iterator<String> fileNames = request.getFileNames();
        MultipartFile[] files = new MultipartFile[5];
        /* Bitta productni 5 tagacha img i bo'lishi mumkin  */

        int i = 0;
        while (fileNames.hasNext()) {

            if (i >= files.length)
                return new Result("It is not possible to upload more than 5 images for a single product!", false);
            files[i] = request.getFile(fileNames.next());
            i++;
        }
        List<Integer> listIdOfSavedFiles = new ArrayList<>();
        for (int j = 0; j < files.length; j++) {
            if (files[j] != null && files[j].getSize() != 0) {

                String originalName = files[j].getOriginalFilename();
                long size = files[j].getSize();
                String contentType = files[j].getContentType();

                String[] words = originalName.split("\\.");
                String nickname = UUID.randomUUID().toString().concat("." + words[words.length - 1]);
                Attachment attachment = new Attachment();
                attachment.setSize(size);
                attachment.setContentType(contentType);
                attachment.setOriginalName(originalName);
                attachment.setNickname(nickname);
                Attachment savedAttachment = attachmentRepository.save(attachment);
                Path path = Paths.get(uploadDirectory + "/" + nickname);
                Files.copy(files[j].getInputStream(), path);
                listIdOfSavedFiles.add(savedAttachment.getId());
            }
        }
        if (listIdOfSavedFiles.size() > 0) return new Result(listIdOfSavedFiles, true);
        else return new Result("It is not possible to upload an empty file!", false);
    }


    public Result get(Integer id, HttpServletResponse response) throws IOException {
        Optional<Attachment> optionalAttachment = attachmentRepository.findByIdAndActive(id, true);
        if (optionalAttachment.isPresent()) {
            response.setHeader("Content-Disposition", "attachment; filename=\" " + optionalAttachment.get().getOriginalName() + "\"");
            FileInputStream fileInputStream = new FileInputStream(uploadDirectory + '/' + optionalAttachment.get().getNickname());
            FileCopyUtils.copy(fileInputStream, response.getOutputStream());
        } else return new Result("Such file id not exist!", false);
        return new Result(optionalAttachment.get(), true);
    }

    public Result edit(Integer id, MultipartHttpServletRequest request) throws IOException {
        Optional<Attachment> optionalAttachment = attachmentRepository.findByIdAndActive(id, true);
        if (!optionalAttachment.isPresent()) return new Result("Such file id not exist!", false);

        Iterator<String> fileNames = request.getFileNames();
        /* Aslida bu yerga bitta file yuborilishi kerak, lekin agar kimdir bitta file ni o'zgartiraman deb, o'rniga bir nechta
         * file ni berib yuborsa bu fayllardan yaroqli bo'lganidan birinchisini saqlaydi va o'zgartirldi degan xabarni qaytaradi,
         * boshqa fayllarga etibor xam bermaydi */
        MultipartFile[] files = new MultipartFile[5];
        /* Bitta productni 5 tagacha img i bo'lishi mumkin  */

        int i = 0;
        while (fileNames.hasNext()) {

            if (i >= files.length)
                return new Result("It is not possible to upload more than 5 images for a single product!", false);
            files[i] = request.getFile(fileNames.next());
            i++;
        }
        for (int j = 0; j < i; j++) {
            if (files[j] != null && files[j].getSize() != 0) {
                String originalName = files[j].getOriginalFilename();
                long size = files[j].getSize();
                String contentType = files[j].getContentType();

                String[] words = originalName.split("\\.");
                String nickname = UUID.randomUUID().toString().concat("." + words[words.length - 1]);
                Attachment attachment = optionalAttachment.get();
                attachment.setSize(size);
                attachment.setContentType(contentType);
                attachment.setOriginalName(originalName);
                attachment.setNickname(nickname);
                attachmentRepository.save(attachment);
                Path path = Paths.get(uploadDirectory + "/" + nickname);
                Files.copy(files[j].getInputStream(), path);
                return new Result("Given file successfully edited.", true);
            }
        }
        return new Result("You entered an empty file, It is not possible to enter an empty file!", false);
    }


    public Result delete(Integer id) {
        Optional<Attachment> optionalAttachment = attachmentRepository.findByIdAndActive(id,true);
        if (!optionalAttachment.isPresent()) return new Result("Such file id not exist!",false);
        Attachment attachment = optionalAttachment.get();
        attachment.setActive(false);
        attachmentRepository.save(attachment);
        return new Result("Given file successfully deleted.", true);
    }

}