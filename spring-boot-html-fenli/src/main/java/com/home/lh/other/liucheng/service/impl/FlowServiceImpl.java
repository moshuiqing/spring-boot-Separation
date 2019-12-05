/**
 * /** 金鸽公司源代码，版权归金鸽公司所有。<br>
 * 项目名称 : LYGYZD
 */

package com.home.lh.other.liucheng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.lh.other.liucheng.mapper.FlowMapper;
import com.home.lh.other.liucheng.po.Flow;
import com.home.lh.other.liucheng.po.FlowNode;
import com.home.lh.other.liucheng.service.FlowService;
import com.home.lh.util.LayuiPage;
import com.home.lh.util.systemutil.SimpleUtils;


/**  
 * 概要说明 : 流程接口实现  <br>
 * 详细说明 : 流程接口实现   <br>
 * 创建时间 : 2018年7月19日 上午11:14:18 <br>
 * @author  by liuhao  
 */
@Service
public class FlowServiceImpl implements FlowService
{

    /**  
     * flowMapper:dao注入
     */
    @Autowired
    private FlowMapper flowMapper;

    @Override
    public Integer insert(Flow f)
    {
        if (f == null)
        {
            return -1;
        }
        boolean flag = SimpleUtils.isEmpty(f.getFlowName(), f.getFlowContent(), f.getCreater());

        if (flag)
        {
            return -1;
        }

        return flowMapper.insert(f);
    }

    @Override
    public Integer delete(Flow f)
    {
        if (f == null)
        {
            return -1;
        }
        boolean flag = SimpleUtils.isEmpty(f.getFlowId() + "");

        if (flag)
        {
            return -1;
        }

        return flowMapper.delete(f);
    }

    @Override
    public Integer update(Flow f)
    {

        if (f == null)
        {
            return -1;
        }
        boolean flag = SimpleUtils.isEmpty(f.getFlowName(), f.getFlowId() + "", f.getUpdater());

        if (flag)
        {
            return -1;
        }

        return flowMapper.update(f);
    }

    @Override
    public Integer pageCount(Flow f)
    {

        Integer count = flowMapper.pageCount(f);

        return count;
    }

    @Override
    public List<Flow> pageFound(Flow f, LayuiPage page)
    {

        List<Flow> flows = flowMapper.pageFound(f, page.getStart(), page.getEnd());
        return flows;
    }

    @Override
    public Integer isEnble(Flow f)
    {

        Integer flag = flowMapper.isEnble(f);
        return flag;
    }

    @Override
    public Integer insertNode(FlowNode fn)
    {
        if (fn == null)
        {
            return -1;
        }

        boolean result = SimpleUtils.isEmpty(fn.getFlowId(), fn.getProcessname(), fn.getSetleft(), fn.getSettop(), fn.getStyle(), fn.getCreater());

        if (result)
        {
            return -1;
        }

        return flowMapper.insertNode(fn);
    }

    @Override
    public List<FlowNode> founNode(FlowNode fn)
    {
        if (fn != null && !fn.getFlowId().equals(""))
        {
            return flowMapper.founNode(fn);
        }

        return null;
    }

    @Override
    public String saveFlowNode(List<FlowNode> flowNodews)
    {
        try
        {
            flowMapper.saveFlowNode(flowNodews);
        }
        catch (Exception e)
        {

            e.printStackTrace();
            return "保存失败";
        }

        return "保存成功！";
    }

    @Override
    public String foundStyle(FlowNode flowNode)
    {
        if (flowNode.getId() == null)
        {
            return null;
        }

        return flowMapper.foundStyle(flowNode);
    }

    @Override
    public Integer deleteNode(FlowNode flowNode)
    {
        if (flowNode == null)
        {
            return -1;
        }
        boolean flag = SimpleUtils.isEmpty(flowNode.getId() + "", flowNode.getFlowId());
        if (flag)
        {
            return -1;
        }
        return flowMapper.deleteNode(flowNode);
    }

    @Override
    public FlowNode foundNodeById(FlowNode flowNode)
    {

        return flowMapper.foundNodeById(flowNode);
    }

    @Override
    public Integer saveNodeByid(FlowNode flowNode)
    {

        return flowMapper.saveNodeByid(flowNode);
    }

    @Override
    public List<FlowNode>  foundCode(FlowNode f)
    {
        if (f == null)
        {
            return null;
        }
        boolean result = SimpleUtils.isEmpty(f.getFlowId(), f.getNodeCode());
        if (result)
        {
            return null;
        }

        return flowMapper.foundCode(f);
    }

    @Override
    public List<FlowNode> foundPage(FlowNode f, LayuiPage p)
    {
          
       
        return flowMapper.foundPage(f, p.getStart(), p.getEnd());
    }

    @Override
    public Integer foundCount(FlowNode f)
    {
          
        
        return flowMapper.foundCount(f);
    }

    @Override
    public Integer insertzdNode(FlowNode f)
    {
          
        
        return flowMapper.insertzdNode(f);
    }

}
