package com.gms.repository;

import com.gms.entity.db.EmailoutboxEntity;
import java.util.List;
import javax.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvThanh
 */
@Repository
public interface EmailoutboxRepository extends CrudRepository<EmailoutboxEntity, Long> {

    @Override
    public List<EmailoutboxEntity> findAll();

//    @Lock(LockModeType.PESSIMISTIC_READ)
//    @Query("SELECT e FROM EmailoutboxEntity e WHERE e.isRun = false ORDER BY e.createAT ASC LIMIT 1")
    @Query(value = "SELECT * FROM email_outbox e WHERE e.IS_RUN = false ORDER BY e.CREATED_AT ASC LIMIT 1", nativeQuery = true)
    public EmailoutboxEntity findByNotRunForUpdate();

    /**
     * 
     * @author pdThang
     * @param subject
     * @param from
     * @param to
     * @param sendAtFrom
     * @param sendAtTo
     * @param tab
     * @param pageable
     * @return 
     */
    @Query("SELECT e FROM EmailoutboxEntity e WHERE 1 = 1 "
            + " AND (e.subject LIKE CONCAT('%',:subject, '%') OR :subject IS '' OR :subject IS NULL) "
            + " AND (e.from = :from OR :from IS '' OR :from IS NULL) "
            + " AND (e.to = :to OR :to IS '' OR :to IS NULL) "
            + " AND (DATE_FORMAT(e.sendAt, '%Y-%m-%d') >= :sendAtForm OR :sendAtForm IS NULL) "
            + " AND (DATE_FORMAT(e.sendAt, '%Y-%m-%d') <= :sendAtTo OR :sendAtTo IS NULL) "
            + " AND (( :tab = 1 AND e.isRun = true AND e.errorMessage IS NULL) OR :tab <> 1 ) "
            + " AND (( :tab = 2 AND e.errorMessage IS NOT NULL) OR :tab <> 2 ) "
            + " AND (( :tab = 3 AND e.isRun = false AND e.errorMessage IS NULL) OR :tab <> 3 ) "
    )
    public Page<EmailoutboxEntity> find(
            @Param("subject") String subject,
            @Param("from") String from,
            @Param("to") String to,
            @Param("sendAtForm") String sendAtFrom,
            @Param("sendAtTo") String sendAtTo,
            @Param("tab") int tab,
            Pageable pageable);

}
