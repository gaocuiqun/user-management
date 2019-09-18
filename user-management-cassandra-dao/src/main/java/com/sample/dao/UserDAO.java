package com.sample.dao;

import com.datastax.driver.core.*;
import com.datastax.driver.core.exceptions.DriverException;
import com.github.apuex.springbootsolution.runtime.*;
import static com.github.apuex.springbootsolution.runtime.DateFormat.*;
import com.sample.message.*;
import com.github.apuex.springbootsolution.runtime.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.cassandra.core.cql.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class UserDAO {

  private final static Logger logger = LoggerFactory.getLogger(UserDAO.class);
  private final WhereClauseWithUnnamedParams where = new WhereClauseWithUnnamedParams(new CamelToCConverter());
  private Session session;

  @Autowired
  public void setSession(Session session) {
    this.session = session;
  }

  public static class ParamMapper implements QueryParamMapper {
    private final Map<String, TypeConverter> mappers;

    public ParamMapper() {
      Map<String, TypeConverter> map = new HashMap<>();
      map.put("userId", TypeConverters.toJavaTypeConverter("string"));
      map.put("userName", TypeConverters.toJavaTypeConverter("string"));
      map.put("password", TypeConverters.toJavaTypeConverter("string"));
      this.mappers = map;
    }

    public Object map(String name, String value) {
      TypeConverter c = mappers.get(name);
      if(null == c) {
        logger.error("No such a field: {}", name);
      }
      return c.convert(value);
    }

    public boolean exists(String name) {
      return mappers.containsKey(name);
    }
  }
  private final QueryParamMapper paramMapper = new ParamMapper();
  public static class ResultRowMapper implements RowMapper<UserVo> {
    public UserVo mapRow(Row row, int rowNum) throws DriverException {
      UserVo.Builder builder = UserVo.newBuilder();
      if(null != row.getString("UserId")) builder.setUserId(row.getString("UserId"));
      if(null != row.getString("UserName")) builder.setUserName(row.getString("UserName"));
      if(null != row.getString("Password")) builder.setPassword(row.getString("Password"));

      return builder.build();
    }
  }
  private final RowMapper<UserVo> rowMapper = new ResultRowMapper();

  public UserDAO(Session session) {
    this.session = session;
  }

  public int create(CreateUserCmd c) {
    Statement statement = new SimpleStatement("INSERT INTO user(user_id,user_name,password) VALUES (?,?,?)", c.getUserId(),c.getUserName(),c.getPassword());
    session.execute(statement);
    return 1;
  }

  public UserVo retrieve(RetrieveUserCmd c) {
    Statement statement = new SimpleStatement("SELECT user_id, user_name, password FROM user WHERE user_id = ? ", c.getUserId());
    ResultSet rs = session.execute(statement);
    if(!rs.isExhausted())
      return rowMapper.mapRow(session.execute(statement).one(), 0);
    else {
      throw new RuntimeException("not found");
    }
  }

  public int update(UpdateUserCmd c) {
    Statement statement = new SimpleStatement("UPDATE user SET user_name = ?, password = ? WHERE user_id = ?", c.getUserName(), c.getPassword(), c.getUserId());
    session.execute(statement);
    return 1;
  }

  public int delete(DeleteUserCmd c) {
    Statement statement = new SimpleStatement("DELETE FROM user WHERE user_id = ?", c.getUserId());
    session.execute(statement);
    return 1;
  }

  public UserListVo query(QueryCommand q) {
    final List<UserVo> result = new LinkedList<>();
    ResultSet rs = query(q, x -> result.add(x));
    return UserListVo.newBuilder()
        .addAllItems(result)
        .setHasMore(!rs.isExhausted())
        .setPagingState(rs.getExecutionInfo().getPagingState().toString())
        .build();
  }

  private ResultSet query(QueryCommand q, ResultCallback<UserVo> c) {
    String sql = String.format("SELECT user_id, user_name, password FROM user %s ", where.toWhereClause(q));
    logger.info(sql);
    Statement statement = new SimpleStatement(sql, rowMapper, where.toUnnamedParamList(q, paramMapper).toArray());
    final int pageSize = q.getRowsPerPage();
    if(pageSize > 0) statement.setFetchSize(pageSize);
    if(!q.getPagingState().isEmpty()) statement.setPagingState(PagingState.fromString(q.getPagingState()));
    ResultSet rs = session.execute(statement);
    if(pageSize > 0) {
      // paginated
      for (int i = 0; i < pageSize && !rs.isExhausted(); ++i) {
        c.add(rowMapper.mapRow(rs.one(), i));
      }
    } else {
      rs.forEach(row -> c.add(rowMapper.mapRow(row, 0)));
    }
    return rs;
  }
}
