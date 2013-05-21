package org.siemac.metamac.srm.web.client.widgets;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

public class CustomSectionStack extends SectionStack {

    protected SectionStackSection defaultSection;

    public CustomSectionStack(String title) {
        this(title, "customSectionStackStyle");
    }

    public CustomSectionStack(String title, String styleName) {
        super();
        setStyleName(styleName);
        JSOHelper.setAttribute(this.getConfig(), "notifyAncestorsOnReflow", true); // BUGFIX to Scroll-back in SectionStacks
        setWidth100();
        setVisibilityMode(VisibilityMode.MULTIPLE);
        setAnimateSections(true);
        setOverflow(Overflow.VISIBLE);
        setHeight(26);
        defaultSection = new SectionStackSection(title);

        setSections(defaultSection);
    }

    public SectionStackSection getDefaultSection() {
        return defaultSection;
    }
}
