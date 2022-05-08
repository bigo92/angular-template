package vnpt.net.syndata.dao;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vnpt.net.syndata.utils.EJson;
import vnpt.net.syndata.utils.Utils;

@Repository
public class NotiStayDao extends BaseDao {

    private String tableName = "DATA_DIODE_SYNC_NOTI_STAY";

    public void addPull(EJson payload) {
        String sql = "INSERT INTO " + tableName + "(GUID, PAYLOAD)"
                + "VALUES (:guid, :payload)";
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        sqlParameterSource.addValue("guid", id);
        sqlParameterSource.addValue("payload", payload.jsonString());

        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public void addMerge(String payload, String payloadMerge, String payloadMergeRef, Number statusRef) {
        String sql = "INSERT INTO " + tableName + "_MERGE (GUID, PAYLOAD, PAYLOAD_MERGE, PAYLOAD_MERGE_REF, PROCESS_STATUS_REF)"
                + "VALUES (:guid, :payload, :payloadMerge, :payloadMergeRef, :statusRef)";
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        sqlParameterSource.addValue("guid", id);
        sqlParameterSource.addValue("payload", payload);
        sqlParameterSource.addValue("payloadMerge", payloadMerge);
        sqlParameterSource.addValue("payloadMergeRef", payloadMergeRef);
        sqlParameterSource.addValue("statusRef", statusRef);

        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public LocalDateTime getLastTime() {
        String sql = "select * from " + tableName + " where IS_MAX = 1 and rownum <= 1";
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();

        List<Map<String, Object>> data = namedParameterJdbcTemplate.queryForList(sql, sqlParameterSource);
        for (Map<String, Object> item : data) {
            return Utils.convertToLocalDate((Date) item.get("UPDATED_DATE"));
        }
        return null;
    }

    @Transactional("transactionManager")
    public void lockDataMerge(String processId, Number size, Number jobTimeOut) {
        String sql = "UPDATE " + tableName
                + " SET PROCESS_ID = :processId, PROCESS_DATE = SYSTIMESTAMP, PROCESS_STATUS = 1"
                + " WHERE PROCESS_ID is null AND"
                + " NOT EXISTS(select 1 from " + tableName
                + "_MERGE m where m.ID = ID AND (m.PROCESS_STATUS <> 3 OR m.PROCESS_STATUS_REF <> 3)) AND"
                + " rownum <= :size";

        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("processId", processId);
        sqlParameterSource.addValue("size", size);

        int result = namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        if (result < size.longValue()) {
            // nếu dữ liệu mới mà ko đủ với size cần lock thì check thêm xem có task nào quá
            // hạn không?
            LocalDateTime timeDelay = LocalDateTime.now();
            timeDelay = timeDelay.plusMinutes(jobTimeOut.longValue() * -1);

            sql = "UPDATE " + tableName
                    + " SET PROCESS_ID = :processId, PROCESS_DATE = SYSTIMESTAMP, PROCESS_STATUS = 1"
                    + " WHERE PROCESS_STATUS in (1,2) AND PROCESS_DATE <= :timeDelay AND"
                    + " NOT EXISTS(select 1 from " + tableName
                    + "_MERGE m where m.ID = ID AND (m.PROCESS_STATUS <> 3 OR m.PROCESS_STATUS_REF <> 3)) AND"
                    + " rownum <= :size";

            sqlParameterSource = new MapSqlParameterSource();
            sqlParameterSource.addValue("processId", processId);
            sqlParameterSource.addValue("timeDelay", Utils.convertToDate(timeDelay));
            sqlParameterSource.addValue("size", size.longValue() - result);

            namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        }
    }

    public Map<String, Object> getDataMergeCurent(Number id) {
        String sql = "select * from " + tableName + "_MERGE where ID = :id and rownum <= 1";
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("id", id);

        List<Map<String, Object>> data = namedParameterJdbcTemplate.queryForList(sql, sqlParameterSource);
        for (Map<String, Object> item : data) {
            return item;
        }
        return null;
    }

    public List<Map<String, Object>> getLockDataMerge(String processId) {
        String sql = "select * from " + tableName + " where PROCESS_ID = :processId";
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("processId", processId);

        return namedParameterJdbcTemplate.queryForList(sql, sqlParameterSource);
    }

    public void unLockDataMerge(String guid, Number status, String mess) {
        String sql = "UPDATE " + tableName + " SET PROCESS_STATUS = :status, PROCESS_MESS = :mess"
                + " WHERE GUID = :guid";
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("guid", guid);
        sqlParameterSource.addValue("status", status);
        sqlParameterSource.addValue("mess", mess);

        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public void updateResult(Number id, String payloadEnd, String payloadFrom) {
        String sql = "UPDATE " + tableName + "_MERGE SET PAYLOAD = :payloadEnd, PAYLOAD_MERGE = :payloadFrom"
                + " PROCESS_STATUS = 3 WHERE id = :id";
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("id", id);
        sqlParameterSource.addValue("payloadEnd", payloadEnd);
        sqlParameterSource.addValue("payloadFrom", payloadFrom);

        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

}
