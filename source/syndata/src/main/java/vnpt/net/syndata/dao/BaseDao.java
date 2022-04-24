package vnpt.net.syndata.dao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vnpt.net.syndata.configuration.SpringMVCConfiguration;

@Repository
@Transactional("transactionManager")
public class BaseDao extends SpringMVCConfiguration {

    @Autowired
    @Qualifier("namedParameterJdbcTemplate")
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public List<Map<String, Object>> getScheduleSetting() throws Exception {
        String sql = "select setting_id,        job_group,          name_job,       " +
                "            time_cross,        ip_server,          json_param,     " +
                "            type_job,          type_run                            " +
                "  from SCHEDULE_SETTING                                            ";
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        try{
            return namedParameterJdbcTemplate.queryForList(sql, sqlParameterSource);
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    /*Job multi insert bình thường*/
    public int addProcessingMulti(long settingId, String transactionId, String serverIp, int isLast) {
        String sql = "INSERT INTO SCHEDULE_PROCESSING                   " +
                "  (SETTING_ID, TRANSACTION_ID, SERVER_IP, IS_LAST)     " +
                "VALUES                                                 " +
                "  (:settingId, :transactionId, :serverIp, :isLast)     ";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("settingId", settingId);
        parameterSource.addValue("transactionId", transactionId);
        parameterSource.addValue("serverIp", serverIp);
        parameterSource.addValue("isLast", isLast); //1:is last,  default 0
        try{
            return namedParameterJdbcTemplate.update(sql, parameterSource);
        }catch (Exception e){
            return -1;
        }
    }

    /*Single theo id job. Nếu job chưa đăng ký thì đăng ký, ngược lại không*/
    public int addProcessingSingle(long settingId, String transactionId, String serverIp, int isLast) {
        String sql = "MERGE INTO SCHEDULE_PROCESSING a                   " +
                "     USING (SELECT count(1) as count_job                " +
                "              FROM SCHEDULE_PROCESSING t                " +
                "             WHERE t.setting_id = :settingId            " +
                "               AND rownum <= 1) b                       " +
                "     ON (b.count_job > 0)                               " +
                "WHEN NOT MATCHEd THEN                                   " +
                "  INSERT                                                " +
                "    (setting_id, transaction_id, server_ip, is_last)    " +
                "  VALUES                                                " +
                "    (:settingId, :transactionId, :serverIp, :isLast)    ";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("settingId", settingId);
        parameterSource.addValue("transactionId", transactionId);
        parameterSource.addValue("serverIp", serverIp);
        parameterSource.addValue("isLast", isLast); //1:is last,  default 0
        try{
            return namedParameterJdbcTemplate.update(sql, parameterSource);
        }catch (Exception e){
            return -1;
        }
    }

    public Map<String, Object> getScheduleProcessing(String transactionId) throws Exception {
        String sql = "select stt.setting_id,    stt.name_job,       stt.time_cross,      " +
                "       stt.ip_server,          stt.json_param,     stt.type_job,        " +
                "       stt.type_run,           stt.job_group,      pro.is_last,         " +
                "       pro.processing_id                                                " +
                "  from SCHEDULE_SETTING stt                                             " +
                " inner join SCHEDULE_PROCESSING pro                                     " +
                "    on stt.setting_id = pro.setting_id                                  " +
                "where pro.transaction_id = :transactionId and rownum <= 1               ";
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("transactionId", transactionId);
        try{
            return namedParameterJdbcTemplate.queryForMap(sql, sqlParameterSource);
        }catch (Exception e){
            return new HashMap<>();
        }
    }

    public int notIsLast(long processingId){
        String sql = "update SCHEDULE_PROCESSING pro            " +
                "        set pro.is_last = 0                    " +
                "      where pro.processing_id = :processingId  ";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("processingId", processingId);
        try{
            return namedParameterJdbcTemplate.update(sql, parameterSource);
        }catch (Exception e){
            return -1;
        }
    }

    public int delScheduleProcessing(String transactionId){
        String sql = "DELETE SCHEDULE_PROCESSING pro                " +
                "      where pro.transaction_id = :transactionId    ";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("transactionId", transactionId);
        try{
            return namedParameterJdbcTemplate.update(sql, parameterSource);
        }catch (Exception e){
            return -1;
        }
    }
}