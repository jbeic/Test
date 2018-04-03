package com.jbeic.mybatis;

public class UserBean
{
  private int id;
  private String username;
  private String password;
  private double account;

  public int getId()
  {
    return this.id; }

  public void setId(int id) {
    this.id = id; }

  public String getUsername() {
    return this.username; }

  public void setUsername(String username) {
    this.username = username; }

  public String getPassword() {
    return this.password; }

  public void setPassword(String password) {
    this.password = password; }

  public double getAccount() {
    return this.account; }

  public void setAccount(double account) {
    this.account = account;
  }

  public String toString() {
    return "UserBean{id=" + 
      this.id + 
      ", username='" + this.username + '\'' + 
      ", password=" + this.password + 
      '}';
  }
}