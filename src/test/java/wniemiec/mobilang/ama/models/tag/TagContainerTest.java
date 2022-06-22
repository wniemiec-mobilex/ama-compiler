package wniemiec.mobilang.ama.models.tag;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TagContainerTest {

    TagContainer tagContainer;
    
    @Test
    void testAddSibling() {
        Tag htmlTag = Tag.getNormalInstance("html");
        Tag divTag = Tag.getNormalInstance("div");
        Tag pTag = Tag.getNormalInstance("p");

        tagContainer = new TagContainer(divTag, htmlTag);

        htmlTag.addChild(divTag);
        tagContainer.addSibling(pTag);

        Assertions.assertEquals(
            List.of(divTag, pTag),
            tagContainer.getParent().getChildren()
        );
        Assertions.assertEquals(htmlTag, pTag.getParent());
    }

    @Test
    void testReplaceTagTo() {
        Tag htmlTag = Tag.getNormalInstance("html");
        Tag divTag = Tag.getNormalInstance("div");
        Tag pTag = Tag.getNormalInstance("p");

        tagContainer = new TagContainer(divTag, htmlTag);

        htmlTag.addChild(divTag);
        tagContainer.replaceTagTo(pTag);

        Assertions.assertEquals(
            List.of(pTag),
            tagContainer.getParent().getChildren()
        );
        Assertions.assertEquals(htmlTag, pTag.getParent());
    }
}
