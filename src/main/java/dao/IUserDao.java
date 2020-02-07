package dao;

import patterns.memento.Memento;

// 用户接口
public interface IUserDao {

    //用户注册，添加用户
    int addUser(Memento user);

    //查询是否有此用户信息，避免注册相同的用户账号
    int selectHasUser(String uid);

    //查询用户信息，返回一个用户的实体类
    Memento findUserById(String id);

    //根据id修改用户信息，将其封装成一个用户对象
    int updateUserById(Memento user);

}
