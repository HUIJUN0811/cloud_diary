package com.msbjy.note.service;

import cn.hutool.core.util.StrUtil;
import com.msbjy.note.dao.TypeDao;
import com.msbjy.note.po.NoteType;
import com.msbjy.note.vo.ResultInfo;

import java.util.List;

public class NoteTypeService {
    private TypeDao typeDao=new TypeDao();

    public List<NoteType> findTypeList(Integer userId){
        List<NoteType> typeList=typeDao.findTypeListByUserId(userId);
        return typeList;
    }

    public ResultInfo<NoteType> delete(String typeId) {
        ResultInfo<NoteType> resultInfo=new ResultInfo();
//        1. 判断参数是否为空
        if (StrUtil.isBlank(typeId)){
            resultInfo.setCode(0);
            resultInfo.setMsg("错误");
            return resultInfo;
        }
//        2. 调用Dao层，通过类型ID查询云记记录的数量
        long noteCount=typeDao.findNoteCountByTypeId(typeId);
//        3. 如果云记数量大于0，说明存在子记录，不可删除
//                code=0，msg="该类型存在子记录，不可删除"，返回resultInfo对象
        if(noteCount>0){
            resultInfo.setCode(0);
            resultInfo.setMsg("该类型存在子记录，不可删除");
            return resultInfo;
        }
//        4. 如果不存在子记录，调用Dao层的更新方法，通过类型ID删除指定的类型记录，返回受影响的行数
        int row = TypeDao.deleteTypeById(typeId);
//        5. 判断受影响的行数是否大于0
//        大于0，code=1；否则，code=0，msg="删除失败"
        if(row>0){
            resultInfo.setCode(1);
        }else {
            resultInfo.setCode(0);
            resultInfo.setMsg("删除失败");
        }
//        6. 返回ResultInfo对象
           return resultInfo;
    }


    public ResultInfo<Integer> addOrUpdate(String typeName, Integer userId,String typeId) {
        ResultInfo<Integer> resultInfo=new ResultInfo<>();
//        1. 判断参数是否为空 （类型名称）
//        如果为空，code=0，msg=xxx，返回ResultInfo对象
        if(StrUtil.isBlank(typeName)){
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名称为空");
            return  resultInfo;
        }
//        2. 调用Dao层，查询当前登录用户下，类型名称是否唯一，返回0或1
        Integer code = typeDao.checkTypeName(typeName, userId, typeId);
//        如果不可用，code=0，msg=xxx，返回ResultInfo对象
        if(code==0){
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名称不可用");
            return  resultInfo;
        }
        Integer key=null;
//        3. 判断类型ID是否为空
        if(StrUtil.isBlank(typeId)){
//        如果为空，调用Dao层的添加方法，返回主键 （前台页面需要显示添加成功之后的类型ID）
            key=typeDao.addType(userId,typeName);
//        如果不为空，调用Dao层的修改方法，返回受影响的行数
          }else {
            key=typeDao.updateType(typeId,typeName);
        }
//        4. 判断 主键/受影响的行数 是否大于0
        if(key>0) {
//        如果大于0，则更新成功
            resultInfo.setCode(1);
//                code=1，result=主键
            resultInfo.setResult(key);
        }else {
//        如果不大于0，则更新失败
//                code=0，msg=xxx
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败");
        }
        return resultInfo;
    }
}
