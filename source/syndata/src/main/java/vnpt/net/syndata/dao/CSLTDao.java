package vnpt.net.syndata.dao;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import vnpt.net.syndata.utils.EJson;

@Repository
public class CSLTDao extends BaseDao {

    public void insertCslt(EJson payload) {
        String sql = "INSERT INTO DATA_DIODE_SYNC_ACCOM_FACILITY(GUID, PAYLOAD)"
                + "VALUES (:guid, :payload)";
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        sqlParameterSource.addValue("guid", id);
        sqlParameterSource.addValue("payload", payload.jsonString());

        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public LocalDateTime getTime(String tableName) {
        String sql = "select * from DATA_DIODE_TIME where TABLE_NAME = :tableName and IS_LAST = 1 and rownum <= 1";
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("tableName", tableName);

        List<Map<String, Object>> customers = namedParameterJdbcTemplate.queryForList(sql, sqlParameterSource);
        for (Map<String, Object> item : customers) {
            return (LocalDateTime) item.get("UPDATE_DATE");
        }
        return null;
    }

    public void insertTime(String tableName, LocalDateTime updateDate) {
        String sql = "INSERT INTO DATA_DIODE_TIME(GUID, TABLE_NAME, UPDATE_DATE)"
                + "VALUES (:guid, :tableName, :updateDate)";
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        sqlParameterSource.addValue("guid", id);
        sqlParameterSource.addValue("tableName", tableName);
        sqlParameterSource.addValue("updateDate", updateDate);

        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }
}
