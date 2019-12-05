/**
 * /** 金鸽公司源代码，版权归金鸽公司所有。<br>
 * 项目名称 : LYGYZD
 */

package com.home.lh.other.liucheng.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.home.lh.other.liucheng.po.Flow;
import com.home.lh.other.liucheng.po.FlowNode;


/**  
 * 概要说明 : 流程dao  <br>
 * 详细说明 : 流程dao  <br>
 * 创建时间 : 2018年7月19日 上午10:57:57 <br>
 * @author  by liuhao  
 */
public interface FlowMapper
{
    /**  
     * 概要说明 : 新增 <br>
     * 详细说明 : 新增<br>
     *
     * @param f  实体
     * @return  Integer 类型返回值说明
     * @see  com.jinge.portal.mapper.FlowMapper#insert()
     * @author  by liuhao @ 2018年7月19日, 上午11:01:24 
     */
    Integer insert(Flow f);
    
    /**  
     * 概要说明 : 删除 <br>
     * 详细说明 : 删除 <br>
     *
     * @param f  实体
     * @return  Integer 类型返回值说明
     * @see  com.jinge.portal.mapper.FlowMapper#delete()
     * @author  by liuhao @ 2018年7月19日, 上午11:03:40 
     */
    Integer delete(Flow f);
    
    /**  
     * 概要说明 : 修改 <br>
     * 详细说明 : 修改 <br>
     * 
     * @param f  实体
     * @return  Integer 类型返回值说明
     * @see  com.jinge.portal.mapper.FlowMapper#update()
     * @author  by liuhao @ 2018年7月19日, 上午11:05:03 
     */
    Integer update(Flow f);
    
    /**  
     * 概要说明 : 查询数量 <br>
     * 详细说明 : 查询数量 <br>
     *
     * @param f  实体
     * @return  Integer 类型返回值说明
     * @see  com.jinge.portal.mapper.FlowMapper#pageCount()
     * @author  by liuhao @ 2018年7月19日, 上午11:06:11 
     */
    Integer pageCount(Flow f);
    
    /**  
     * 概要说明 : 分页查询 <br>
     * 详细说明 : 分页查询 <br>
     *
     * @param f  实体
     * @param page  页码
     * @param limit  条数
     * @return  List<Flow> 类型返回值说明
     * @see  com.jinge.portal.mapper.FlowMapper#pageFound()
     * @author  by liuhao @ 2018年7月19日, 上午11:07:01 
     */
    List<Flow> pageFound(Flow f,@Param("start")Integer page,@Param("end")Integer limit);
    
    /**  
     * 概要说明 : 激活or禁用 <br>
     * 详细说明 :  激活or禁用<br>
     *
     * @param f  实体
     * @return  Integer 类型返回值说明
     * @see  com.jinge.portal.mapper.FlowMapper#isEnble()
     * @author  by liuhao @ 2018年7月19日, 上午11:09:14 
     */
    Integer isEnble(Flow f);
    
    
    
    ///////////////////////////////********************************************************************************/////
    /**  
     * 概要说明 : 新增节点 <br>
     * 详细说明 : 新增节点 <br>
     *
     * @param fn  实体
     * @return  Integer 类型返回值说明
     * @see  com.jinge.portal.mapper.FlowMapper#insertNode()
     * @author  by liuhao @ 2018年7月19日, 下午4:28:42 
     */
    Integer insertNode(FlowNode fn);
    
    
    /**  
     * 概要说明 : 查询全部节点 <br>
     * 详细说明 : 查询全部节点 <br>
     *
     * @param fn 实体
     * @return  List<FlowNode> 类型返回值说明
     * @see  com.jinge.portal.mapper.FlowMapper#founNode()
     * @author  by liuhao @ 2018年7月19日, 下午5:00:36 
     */
    List<FlowNode> founNode(FlowNode fn);
    
    
    /**  
     * 概要说明 : 批量保存信息 <br>
     * 详细说明 : 批量保存信息  <br>
     *
     * @param nodelist  void 类型返回值说明
     * @see  com.jinge.portal.mapper.FlowMapper#saveFlowNode()
     * @author  by liuhao @ 2018年7月19日, 下午5:30:29 
     */
    void saveFlowNode(@Param("nodelist")List<FlowNode> nodelist);
    
    /**  
     * 概要说明 : 查询样式 <br>
     * 详细说明 : 查询样式 <br>
     *
     * @param flowNode  实体
     * @return  String 类型返回值说明
     * @see  com.jinge.portal.mapper.FlowMapper#foundStyle()
     * @author  by liuhao @ 2018年7月19日, 下午5:50:43 
     */
    String foundStyle(FlowNode flowNode);
    
    
    /**  
     * 概要说明 : 删除节点 <br>
     * 详细说明 : 删除节点 <br>
     *
     * @param flowNode  实体
     * @return  Integer 类型返回值说明
     * @see  com.jinge.portal.mapper.FlowMapper#deleteNode()
     * @author  by liuhao @ 2018年7月20日, 上午8:54:52 
     */
    Integer deleteNode(FlowNode flowNode);
    
    
    /**  
     * 概要说明 : 根据id查询<br>
     * 详细说明 : 根据id查询<br>
     *
     * @param flowNode  实体
     * @return  FlowNode 类型返回值说明
     * @see  com.jinge.portal.mapper.FlowMapper#foundNodeById()
     * @author  by liuhao @ 2018年7月20日, 上午9:58:07 
     */
    FlowNode foundNodeById(FlowNode flowNode);
    
    /**  
     * 概要说明 : 根据id保存信息 <br>
     * 详细说明 : 根据id保存信息  <br> 
     *
     * @param flowNode 实体
     * @return  Integer 类型返回值说明
     * @see  com.jinge.portal.mapper.FlowMapper#saveNodeByid()
     * @author  by liuhao @ 2018年7月20日, 下午1:37:16 
     */
    Integer saveNodeByid(FlowNode flowNode);
    
    /**  
     * 概要说明 : 判断编码是否重复 <br>
     * 详细说明 : 判断编码是否重复 <br>
     *
     * @param flowNode  实体
     * @return  Integer 类型返回值说明
     * @see  com.jinge.portal.mapper.FlowMapper#foundCode()
     * @author  by liuhao @ 2018年7月23日, 上午11:30:18 
     */
    List<FlowNode> foundCode(FlowNode flowNode);
    
    
    
    /**  
     * 概要说明 : 分页查询 <br>
     * 详细说明 : 分页查询  <br>
     *
     * @param f 实体
     * @param startIndex  分页参数
     * @param page   分页参数
     * @return  List<FlowNode> 类型返回值说明
     * @see  com.jinge.portal.mapper.FlowMapper#foundPage()
     * @author  by liuhao @ 2018年7月23日, 下午3:22:11 
     */
    List<FlowNode> foundPage(@Param("f")FlowNode f,@Param("start")Integer startIndex,@Param("end")Integer page);
    
    /**  
     * 概要说明 : 查询数量 <br>
     * 详细说明 : 查询数量 <br>
     *
     * @param f  实体
     * @return  Integer 类型返回值说明
     * @see  com.jinge.portal.mapper.FlowMapper#foundCount()
     * @author  by liuhao @ 2018年7月23日, 下午3:23:39 
     */
    Integer foundCount(FlowNode f);
    
    
    /**  
     * 概要说明 : 字典新增 <br>
     * 详细说明 : 字典新增 <br>
     *
     * @param f  实体
     * @return  Integer 类型返回值说明
     * @see  com.jinge.portal.mapper.FlowMapper#insertzdNode()
     * @author  by liuhao @ 2018年7月24日, 上午9:12:06 
     */
    Integer insertzdNode(FlowNode f);
}
