package demo1.pojo;
import java.util.HashMap;

public class AjaxResult extends HashMap<String,Object>  {
    public final static String CODE_TAG = "httpStatus";
    public final static String MSG_TAG = "msg";
    public final static String DATA_TAG = "data";


   public AjaxResult(){

   }


    public AjaxResult(Type type){
        super.put(CODE_TAG,type.getValue());
        super.put(MSG_TAG,type.getMsg());
    }

    public AjaxResult(Type type, String msg){
       super.put(CODE_TAG,type.getValue());
       super.put(MSG_TAG,msg);

    }

    public AjaxResult(Type type,Object data) {
        super.put(CODE_TAG, type.getValue());
        super.put(MSG_TAG, type.getMsg());
        if (data != null &&  !"".equals(data)) {
            super.put(DATA_TAG, data);
        }
    }

    public AjaxResult(Type type,String msg,Object data) {
        super.put(CODE_TAG, type.getValue());
        super.put(MSG_TAG, msg);
        if (data != null &&  !"".equals(data)) {
            super.put(DATA_TAG, data);
        }
    }


    public static AjaxResult success(String msg,Object data){
       return new AjaxResult(Type.SUCCESS,msg,data);
    }

    public static AjaxResult success(Object data){
       return new AjaxResult(Type.SUCCESS,data);
    }

    public static AjaxResult failure(Type type,String msg){
       return new AjaxResult(type,msg);
    }

    public static AjaxResult failure(Type type,String msg,Object data){
       return new AjaxResult(type,msg,data);
    }

    public static AjaxResult failure(Type type){
       return new AjaxResult(type);
    }










}
