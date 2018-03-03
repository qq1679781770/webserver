package org.jsxnh.view.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsxnh.view.dao.BaseDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KyService {


    public JSONArray getQuestions(){
        JSONArray jsonArray = new JSONArray();
        try {
            BaseDao baseDao = new BaseDao();
            Connection connection = baseDao.getconnect();
            String sql = "select * from question_ky";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet res=preparedStatement.executeQuery();
            while(res.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("question",res.getString(2));
                jsonObject.put("question_id",res.getInt(3));
                jsonArray.put(jsonObject);
            }
            res.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonArray;

    }

}
