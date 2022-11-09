package com.narsha.wave.domain.repository;

import com.narsha.wave.domain.entity.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DataRepository extends JpaRepository<Data, Long> {

    @Query(value = "SELECT " +
            "0 AS idx, " +
            "sysdate() AS save_time, " +
            "device_id, " +
            "AVG(attention) AS attention," +
            "AVG(delta) AS delta," +
            "AVG(high_alpha) AS high_alpha," +
            "AVG(high_beta) AS high_beta," +
            "AVG(low_alpha) AS low_alpha," +
            "AVG(low_beta) AS low_beta," +
            "AVG(low_gamma) AS low_gamma," +
            "AVG(meditation) AS meditation," +
            "AVG(mid_gamma) AS mid_gamma," +
            "AVG(theta) AS theta" +
            " FROM tb1_noepa_data WHERE device_id=? GROUP BY device_id", nativeQuery = true)
    Optional<Data> findAvgByDeviceId(int deviceId);

    Optional<Data> findFirstByDeviceIdOrderBySaveTimeDesc(int deviceId);

}