package org.siemac.metamac.srm.web.code.widgets;

import java.util.Comparator;

import org.siemac.metamac.srm.web.code.model.ds.CodeDS;

import com.smartgwt.client.widgets.tree.TreeNode;

public class CodeTreeNode extends TreeNode implements Comparable<CodeTreeNode> {

    private Long order;

    public CodeTreeNode(String name) {
        super(name);
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
        setAttribute(CodeDS.ORDER, order);
    }

    @Override
    public int compareTo(CodeTreeNode o) {
        Long value = this.getOrder() - o.getOrder();
        return value.intValue();
    }

    public static Comparator<CodeTreeNode> OrderComparator = new Comparator<CodeTreeNode>() {

                                                               @Override
                                                               public int compare(CodeTreeNode codeNode1, CodeTreeNode codeNode2) {

                                                                   Long order1 = codeNode1.getOrder();
                                                                   Long order2 = codeNode2.getOrder();

                                                                   // Ascending order
                                                                   return order1.compareTo(order2);
                                                               }
                                                           };
}
