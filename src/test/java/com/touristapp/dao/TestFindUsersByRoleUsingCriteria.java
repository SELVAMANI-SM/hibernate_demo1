package com.touristapp.dao;

import java.util.List;

import com.touristapp.model.User;

public class TestFindUsersByRoleUsingCriteria {

	public static void main(String[] args) {
		List<User> users = UserDAO.getUsers("USER");
		for (User user : users) {
			System.out.println(user);
		}
	}
}
