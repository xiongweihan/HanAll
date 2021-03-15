package com.example.hanall.widget.cehuashanchu;

/**
 * 管理侧滑item的帮助类
 */
public class DragItemHelper {
    private ItemDragLayout openedItemLayout;
    private static final DragItemHelper instance = new DragItemHelper();

    private DragItemHelper() {

    }

    public static DragItemHelper getInstance() {
        return instance;
    }

    public ItemDragLayout getOpenedItemLayout() {
        return openedItemLayout;
    }

    public void setOpenedItemLayout(ItemDragLayout openedItemLayout) {
        this.openedItemLayout = openedItemLayout;
    }

    public void removeItemDragLayout() {
        if (openedItemLayout != null && openedItemLayout.getState() == ItemDragLayout.State.OPEN) {
            openedItemLayout.close();
        }
        openedItemLayout = null;
    }

    public void closeItemDragLayout() {
        if (openedItemLayout != null) {
            openedItemLayout.close();
        }
    }

    public boolean canDrag(ItemDragLayout itemDragLayout) {
        return openedItemLayout == null || itemDragLayout == openedItemLayout;

    }
}
