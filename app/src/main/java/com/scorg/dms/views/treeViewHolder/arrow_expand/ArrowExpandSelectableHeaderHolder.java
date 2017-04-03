package com.scorg.dms.views.treeViewHolder.arrow_expand;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.scorg.dms.R;
import com.unnamed.b.atv.model.TreeNode;

/**
 *
 */
public class ArrowExpandSelectableHeaderHolder extends TreeNode.BaseNodeViewHolder<ArrowExpandIconTreeItemHolder.IconTreeItem> {
    private int nodeValueColor;
    private boolean isTreeLabelBold;
    private int leftPadding;
    private boolean isDefaultExpanded;
    private TextView tvValue;
    private PrintView arrowView;
    private CheckBox nodeSelector;
    private LinearLayout mainContentLayout;
    private boolean isParentNodeChecked;

    public ArrowExpandSelectableHeaderHolder(Context context, boolean isDefaultExpanded) {

        this(context, isDefaultExpanded, (int) (context.getResources().getDimension(R.dimen.dp10) / context.getResources().getDisplayMetrics().density));
    }

    public ArrowExpandSelectableHeaderHolder(Context context, boolean isDefaultExpanded, int leftPadding) {
        super(context);
        this.leftPadding = leftPadding;
        this.isDefaultExpanded = isDefaultExpanded;
        nodeValueColor = ContextCompat.getColor(context, R.color.black);
    }

    @Override
    public View createNodeView(final TreeNode node, ArrowExpandIconTreeItemHolder.IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.treeview_arrow_expandable_header, null, false);

        mainContentLayout = (LinearLayout) view.findViewById(R.id.mainContentLayout);
        mainContentLayout.setPadding(leftPadding, 0, 0, 0);

        tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setTextColor(getNodeValueColor());

        if (isTreeLabelBold())
            tvValue.setTypeface(null, Typeface.BOLD);
        else
            tvValue.setTypeface(null, Typeface.NORMAL);

        if (value.text.toString().contains("|")) {
            tvValue.setText(value.text.toString().split("\\|")[0]);
        } else {
            tvValue.setText(value.text.toString());
        }
        if (isParentNodeChecked()) {
            mainContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isParentNodeChecked()) {
                        setSelectedChildNodesChecked(node);
                        setParentNodeChecked(false);
                    } else {
                        setSelectedChildNodesChecked(node);
                        setParentNodeChecked(true);

                    }
                }
            });
        }


        arrowView = (PrintView) view.findViewById(R.id.arrow_icon);
        arrowView.setPadding(20, 10, 10, 10);
        if (node.isLeaf()) {
            arrowView.setVisibility(View.INVISIBLE);
        }
        arrowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tView.toggleNode(node);
            }
        });

        nodeSelector = (CheckBox) view.findViewById(R.id.node_selector);
        nodeSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isParentNodeChecked()) {
                    setSelectedChildNodesChecked(node);
                    setParentNodeChecked(false);
                } else {
                    setSelectedChildNodesChecked(node);
                    setParentNodeChecked(true);

                }
            }

        });

        node.setExpanded(isDefaultExpanded);

        return view;
    }

    private void setSelectedChildNodesChecked(TreeNode node) {
        node.setSelected(isParentNodeChecked());
        for (TreeNode n : node.getChildren()) {
            getTreeView().selectNode(n, isParentNodeChecked());
        }
        nodeSelector.setChecked(node.isSelected());
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }

    @Override
    public void toggleSelectionMode(boolean editModeEnabled) {
        nodeSelector.setVisibility(editModeEnabled ? View.VISIBLE : View.INVISIBLE);
        nodeSelector.setChecked(mNode.isSelected());
    }

    public int getNodeValueColor() {
        return nodeValueColor;
    }

    public void setNodeValueColor(int nodeValueColor) {
        this.nodeValueColor = nodeValueColor;
    }

    public boolean isTreeLabelBold() {
        return isTreeLabelBold;
    }

    public void setParentNodeChecked(boolean parentNodeChecked) {
        isParentNodeChecked = parentNodeChecked;
    }

    public boolean isParentNodeChecked() {
        return isParentNodeChecked;
    }

    public void setTreeLabelBold(boolean treeLabelBold) {
        isTreeLabelBold = treeLabelBold;
    }

}
