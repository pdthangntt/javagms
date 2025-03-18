package com.gms.repository;

import com.gms.entity.db.QlReport;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvThành
 */
@Repository
public interface QlReportRepository extends CrudRepository<QlReport, Long> {

    @Override
    public List<QlReport> findAll();

    @Override
    public Optional<QlReport> findById(Long ID);

    @Override
    public QlReport save(QlReport model);

    @Query("SELECT e FROM QlReport e WHERE e.month = :month AND e.year = :year AND e.districtID = :districtID AND e.provinceID = :provinceID AND e.wardID = :wardID")
    public QlReport findOne(
            @Param("provinceID") String provinceID,
            @Param("districtID") String districtID,
            @Param("wardID") String wardID,
            @Param("month") int month,
            @Param("year") int year);

    @Query(value = "SELECT * FROM ql_report as e ORDER BY e.updated_at DESC LIMIT 1", nativeQuery = true)
    public QlReport find();

}
