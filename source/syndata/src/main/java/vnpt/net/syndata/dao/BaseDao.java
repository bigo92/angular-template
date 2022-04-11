package vnpt.net.syndata.dao;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vnpt.net.syndata.configuration.SpringMVCConfiguration;

@Repository
@Transactional("transactionManager")
public class BaseDao extends SpringMVCConfiguration {

    @Autowired
    @Qualifier("namedParameterJdbcTemplate")
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public List testfunction(String param) throws Exception {
        String sql = "select setting_id,\n" +
                "       name_job,\n" +
                "       time_cross,\n" +
                "       ip_server,\n" +
                "       json_param,\n" +
                "       type_job,\n" +
                "       type_run\n" +
                "  from SCHEDULE_SETTING\n";
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        List customers = namedParameterJdbcTemplate.queryForList(sql, sqlParameterSource);

        return customers;
    }
}
