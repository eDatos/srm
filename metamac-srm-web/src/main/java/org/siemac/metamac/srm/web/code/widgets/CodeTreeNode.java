package org.siemac.metamac.srm.web.code.widgets;

import java.util.Comparator;

import org.siemac.metamac.srm.web.client.widgets.CodeNavigableTreeNode;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;

public class CodeTreeNode extends CodeNavigableTreeNode implements Comparable<CodeTreeNode> {

    private Integer order;

    public CodeTreeNode(String name) {
        super();
        setName(name);
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
        setAttribute(CodeDS.ORDER, order);
    }

    @Override
    public int compareTo(CodeTreeNode o) {
        return this.getOrder() - o.getOrder();
    }

    public static Comparator<CodeTreeNode> OrderComparator = new Comparator<CodeTreeNode>() {

                                                               @Override
                                                               public int compare(CodeTreeNode codeNode1, CodeTreeNode codeNode2) {

                                                                   Integer order1 = codeNode1.getOrder();
                                                                   Integer order2 = codeNode2.getOrder();

                                                                   // Ascending order
                                                                   return order1.compareTo(order2);
                                                               }
                                                           };
}
