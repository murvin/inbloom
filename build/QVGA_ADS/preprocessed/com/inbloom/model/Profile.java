package com.inbloom.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.lcdui.Image;

/**
 * A user profile consists of a name, picture and an optional password.
 * 
 * @author M
 * @date 16-Jul-2011
 */
public class Profile implements ISerializable{
    
    private String name;
    
    private byte[] picture;
    
    private int password;
    
    private String forum_user_name;
    
    private String forum_password;
    
    /**
     * Used to get the name of the user profile
     * 
     * @return          name associated with profile
     */
    public String getName(){
        return this.name;
    }
    
    /**
     * Used to set the name of the user
     * 
     * @param name      name associated with profile
     */
    public void setName(String name){
        this.name = name;
    }
    
    /**
     * Used to get the thumbnail picture of the user.
     * 
     * @return          the thumbnail picture
     */
    public byte[] getPicture(){
        return this.picture;
    }
    
    /**
     * Used to set the thumbnail picture.
     * 
     * @param picture   the thumbnail picture.
     */
    public void setPicture(byte[] picture){
        this.picture = picture;
    }
    
    public Image getImgPicture(){
        if(getPicture() != null){
            return Image.createImage(picture, 0, picture.length);
        }
        return null;
    }
    
    /**
     * Used to get the password associated with the profile.
     * 
     * @return          the user password.
     */
    public int getPassword(){
        return this.password;
    }
    
    /**
     * Used to set the password associated with the profile.
     * 
     * @param password  the user password.
     */
    public void setPassword(int password){
        this.password = password;
    }

    public String getForum_password() {
        return forum_password;
    }

    public void setForum_password(String forum_password) {
        this.forum_password = forum_password;
    }

    public String getForum_user_name() {
        return forum_user_name;
    }

    public void setForum_user_name(String forum_user_name) {
        this.forum_user_name = forum_user_name;
    }
    
    public void serialize(DataOutputStream dos) throws IOException {
        dos.writeBoolean(name != null);
        if(name != null){
            dos.writeUTF(name);
        }
        
        dos.writeBoolean(picture != null);
        if(picture != null){
            dos.write(picture.length);
            dos.write(picture);
        }
        
        dos.writeInt(password);
        
        dos.writeBoolean(forum_user_name != null);
        if(forum_user_name != null){
            dos.writeUTF(forum_user_name);
        }
        
        dos.writeBoolean(forum_password != null);
        if(forum_password != null){
            dos.writeUTF(forum_password);
        }
    }

    public void deserialize(DataInputStream dis) throws IOException {
        if(dis.readBoolean()){
            name = dis.readUTF();
        }
        
        if(dis.readBoolean()){
            int pic_length = dis.read();
            picture = new byte[pic_length];
            dis.read(picture);
        }
        
        password = dis.readInt();
        
        if(dis.readBoolean()){
            this.forum_user_name = dis.readUTF();
        }
        
        if(dis.readBoolean()){
            this.forum_password = dis.readUTF();
        }
    }
}
