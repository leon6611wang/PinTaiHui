package com.zhiyu.quanzhu.model.bean;

public class MyCircle {
    private int id;
    private String name;
    private int role;
    private boolean isSelected;
    private String roleName;
    private String thumb;

    @Override
    public String toString() {
        return "id "+id+" , name "+name+" , role "+role+" , roleName "+getRoleName()+" , isSelected "+isSelected;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getRoleName() {
        //0圈主 1管理员 2成员
        switch (this.role){
            case 0:
                this.roleName="圈主";
                break;
            case 1:
                this.roleName="管理员";
                break;
            case 2:
                this.roleName="成员";
                break;
        }
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
