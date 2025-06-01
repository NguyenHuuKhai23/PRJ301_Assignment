/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author knguy
 */
public class User {

    private int userID;
    private String studentID, fullName, email, password, image, role;
    private MemberClub memberClub;
    

    public User() {
    }

    public User(int userID, String studentID, String fullName, String email, String password, String image, String role, MemberClub memberClub) {
        this.userID = userID;
        this.studentID = studentID;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.image = image;
        this.role = role;
        this.memberClub = memberClub;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public MemberClub getMemberClub() {
        return memberClub;
    }

    public void setMemberClub(MemberClub memberClub) {
        this.memberClub = memberClub;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "User{" + "userID=" + userID + ", studentID=" + studentID + ", fullName=" + fullName + ", email=" + email + ", role=" + role + ", password=" + password + ", image=" + image + ", memberClub=" + memberClub.toString() + '}';
    }

}
