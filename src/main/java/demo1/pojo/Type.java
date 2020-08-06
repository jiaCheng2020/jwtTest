package demo1.pojo;


public enum Type{
    SUCCESS(200,"操作成功"),
    UNAUTHORISED(401,"验证失败"),
    ACCESS_DENIED(403,"拒绝访问"),
    ERROR(500,"服务器出错");

    private final int value;
    private String msg;

    Type (int value,String msg){
        this.value=value;
        this.msg = msg;
    }

    int getValue(){
        return this.value;
    }

    String getMsg(){
        return  this.msg;
    }

}