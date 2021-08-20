package uz.cherry.cherry_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.cherry.cherry_server.entity.Attachment;

import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {

Optional<Attachment> findByIdAndActive(Integer id, boolean active);
}
