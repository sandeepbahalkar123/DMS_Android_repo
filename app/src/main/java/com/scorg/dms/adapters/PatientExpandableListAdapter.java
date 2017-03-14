package com.scorg.dms.adapters;

import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.model.responsemodel.showsearchresultresponsemodel.PatientFileData;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DmsConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.value;

/**
 * Created by riteshpandhurkar on 24/2/17.
 */

public class PatientExpandableListAdapter extends BaseExpandableListAdapter implements CompoundButton.OnCheckedChangeListener {

    private String TAG = this.getClass().getName();
    private Context _context;


    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<PatientFileData>> _listDataChild;

    private List<String> _originalListDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<PatientFileData>> _originalListDataChild;

    // @BindString(R.string.opd)
    String opd;
    // @BindString(R.string.ipd)
    String ipd;
    String uhid;
    int dataShowMaxValue = 2;

    // Hashmap for keeping track of our checkbox check states
    private HashMap<Integer, boolean[]> mChildCheckStates;

    public PatientExpandableListAdapter(Context context, List<String> listDataHeader,
                                        HashMap<String, ArrayList<PatientFileData>> listChildData) {
        this._context = context;
        this._originalListDataHeader = listDataHeader;
        this._originalListDataChild = listChildData;

        manageChild(null);

        opd = _context.getString(R.string.opd);
        ipd = _context.getString(R.string.ipd);
        uhid = _context.getString(R.string.uhid);


    }

    @Override
    public PatientFileData getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final int mGroupPosition = groupPosition;
        final int mChildPosition = childPosition;


        final ChildViewHolder childViewHolder;
        if (convertView == null) {
            LayoutInflater inflaInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflaInflater.inflate(R.layout
                    .item_patient_content, null);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        PatientFileData childElement = getChild(groupPosition, childPosition);

        //---
        if (opd.equalsIgnoreCase(childElement.getFileType())) {
            childViewHolder.opd.setText(opd);
            childViewHolder.opdValue.setText("" + childElement.getReferenceId());
            //---------
            //-- TODO: visit date is not getting from API
            String s = CommonMethods.formatDateTime(childElement.getAdmissionDate(), DmsConstants.DATE_PATTERN.DD_MM_YYYY, DmsConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DmsConstants.DATE);
            childViewHolder.opdVisitDateValue.setText(s);
            //---------
            //+++++++----------

            childViewHolder.opdLayout.setVisibility(View.VISIBLE);
            childViewHolder.ipdLayout.setVisibility(View.GONE);

        } else {

            childViewHolder.ipd.setText(ipd);
            childViewHolder.ipdValue.setText("" + childElement.getReferenceId());

            String date = CommonMethods.formatDateTime(childElement.getAdmissionDate(), DmsConstants.DATE_PATTERN.DD_MM_YYYY, DmsConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DmsConstants.DATE);

            childViewHolder.ipdAdmissionDateValue.setText(date);
            //---
            date = CommonMethods.formatDateTime(childElement.getDischargeDate(), DmsConstants.DATE_PATTERN.DD_MM_YYYY, DmsConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DmsConstants.DATE);

            childViewHolder.ipdDischargeDateValue.setText(date);

            childViewHolder.ipdLayout.setVisibility(View.VISIBLE);
            childViewHolder.opdLayout.setVisibility(View.GONE);
        }

        //------------------
        int originalChildrenCount = getOriginalChildrenCount(groupPosition);
        int shownDataChildrenCount = getChildrenCount(groupPosition);
       // CommonMethods.Log(TAG, "childrenCount: " + groupPosition + ":" + childPosition + ":" + originalChildrenCount);
        if (dataShowMaxValue < originalChildrenCount && (childPosition + 1) == shownDataChildrenCount) {
            childViewHolder.moreOption.setVisibility(View.VISIBLE);
            childViewHolder.moreOption.setTag(getGroup(groupPosition));
            if (childElement.isShowCompleteList()) {
                childViewHolder.moreOption.setText(_context.getString(R.string.less));
            } else {
                childViewHolder.moreOption.setText(_context.getString(R.string.more));
            }
        } else {
            childViewHolder.moreOption.setVisibility(View.GONE);
        }

        childViewHolder.ipdCheckBox.setOnCheckedChangeListener(null);
        childViewHolder.opdCheckBox.setOnCheckedChangeListener(null);

        if (mChildCheckStates.containsKey(mGroupPosition)) {
            boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
            if (childViewHolder.ipdLayout.getVisibility() == View.VISIBLE) {
                childViewHolder.ipdCheckBox.setChecked(getChecked[mChildPosition]);
            } else if (childViewHolder.opdLayout.getVisibility() == View.VISIBLE) {
                childViewHolder.opdCheckBox.setChecked(getChecked[mChildPosition]);
            }
        } else {
            boolean getChecked[] = new boolean[getChildrenCount(mGroupPosition)];

            // add getChecked[] to the mChildCheckStates hashmap using mGroupPosition as the key
            mChildCheckStates.put(mGroupPosition, getChecked);

            // set the check state of this position's checkbox based on the
            // boolean value of getChecked[position]
            if (childViewHolder.ipdLayout.getVisibility() == View.VISIBLE) {
                childViewHolder.ipdCheckBox.setChecked(false);
            } else if (childViewHolder.opdLayout.getVisibility() == View.VISIBLE) {
                childViewHolder.opdCheckBox.setChecked(false);
            }
        }

        childViewHolder.opdCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoxClicked(buttonView, isChecked, mGroupPosition, mChildPosition);
            }
        });
        childViewHolder.ipdCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoxClicked(buttonView, isChecked, mGroupPosition, mChildPosition);
            }
        });
        //--------------------

        //------------------

        childViewHolder.moreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lessText = _context.getString(R.string.less);
                String moreText = _context.getString(R.string.more);
                String textString = childViewHolder.moreOption.getText().toString();
                if (textString.equalsIgnoreCase(lessText)) {
                    manageChild(null);
                    childViewHolder.moreOption.setText(moreText);
                } else {
                    String groupName = (String) v.getTag();
                    manageChild(groupName);
                    childViewHolder.moreOption.setText(lessText);
                }

            }
        });

        //--------------------

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            LayoutInflater inflaInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflaInflater.inflate(R.layout
                    .item_patient_list_header, null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);

        String groupHeader = (String) getGroup(groupPosition);
        int childrenCount = getChildrenCount(groupPosition);

        groupViewHolder.userName.setText("" + groupHeader);
        groupViewHolder.uhid.setText(uhid);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }


    static class GroupViewHolder {
        @BindView(R.id.userName)
        TextView userName;
        @BindView(R.id.uhid)
        TextView uhid;

        public GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        //---------
        @BindView(R.id.opd)
        TextView opd;
        @BindView(R.id.opdValue)
        TextView opdValue;
        @BindView(R.id.opdVisitDate)
        TextView opdVisitDate;
        @BindView(R.id.opdVisitDateValue)
        TextView opdVisitDateValue;
        @BindView(R.id.opdCheckBox)
        CheckBox opdCheckBox;
        //----
        @BindView(R.id.ipd)
        TextView ipd;
        @BindView(R.id.ipdValue)
        TextView ipdValue;
        @BindView(R.id.ipdAdmissionDate)
        TextView ipdAdmissionDate;
        @BindView(R.id.ipdAdmissionDateValue)
        TextView ipdAdmissionDateValue;
        @BindView(R.id.ipdDischargeDate)
        TextView ipdDischargeDate;
        @BindView(R.id.ipdDischargeDateValue)
        TextView ipdDischargeDateValue;
        @BindView(R.id.ipdCheckBox)
        CheckBox ipdCheckBox;
        //----
        @BindView(R.id.opdLayout)
        LinearLayout opdLayout;
        @BindView(R.id.ipdLayout)
        LinearLayout ipdLayout;
        @BindView(R.id.moreOption)
        TextView moreOption;

        public ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    private void manageChild(String groupName) {

        _listDataChild = new HashMap<String, ArrayList<PatientFileData>>();
        mChildCheckStates = new HashMap<Integer, boolean[]>();

        //-----
        _listDataHeader = new ArrayList<>();
        _listDataHeader.addAll(_originalListDataHeader);
        //-------

        HashMap<String, ArrayList<PatientFileData>> listDataChild = _originalListDataChild;

        Iterator it = listDataChild.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry) it.next();
            ArrayList<PatientFileData> value = (ArrayList<PatientFileData>) pair.getValue();

            if (((String) pair.getKey()).equalsIgnoreCase(groupName)) {
                //----------
                for (PatientFileData dataObject :
                        value) {
                    dataObject.setShowCompleteList(true);
                }
                //---------

                _listDataChild.put((String) pair.getKey(), value);
            } else {
                //----------
                for (PatientFileData dataObject :
                        value) {
                    dataObject.setShowCompleteList(false);
                }
                //---------
                if (value.size() > dataShowMaxValue) {
                    ArrayList<PatientFileData> tempList = new ArrayList<PatientFileData>();
                    for (int i = 0; i < dataShowMaxValue; i++) {
                        tempList.add(value.get(i));
                    }
                    _listDataChild.put((String) pair.getKey(), tempList);
                } else {
                    _listDataChild.put((String) pair.getKey(), value);
                }
            }

        }

       // CommonMethods.Log(TAG, _listDataChild.toString());

        this.notifyDataSetChanged();
    }


    private int getOriginalChildrenCount(int groupPosition) {
        return this._originalListDataChild.get(this._originalListDataHeader.get(groupPosition))
                .size();
    }

    private void checkBoxClicked(CompoundButton buttonView, boolean isChecked, int mGroupPosition, int mChildPosition) {
        if (isChecked) {
            //------
            boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
            getChecked[mChildPosition] = isChecked;
            mChildCheckStates.put(mGroupPosition, getChecked);
            //------
            int checkedDataToCompare[] = new int[getChecked.length];

            boolean[] checkedValues = mChildCheckStates.get(mGroupPosition);

            for (int i = 0; i < checkedValues.length; i++) {
                boolean checkedValue = checkedValues[i];
                if (checkedValue == true) {
                    checkedDataToCompare[i] = i;
                }
            }

            if (checkedDataToCompare.length == 1) {
                PatientFileData child = getChild(mGroupPosition, checkedDataToCompare[0]);
                showCompareOptionsDialog("" + child.getReferenceId(), "");
            } else if (checkedDataToCompare.length == 2) {
                PatientFileData child = getChild(mGroupPosition, checkedDataToCompare[0]);
                PatientFileData child_1 = getChild(mGroupPosition, checkedDataToCompare[1]);
                showCompareOptionsDialog("" + child.getReferenceId(), "" + child_1.getReferenceId());
            } else {
                CommonMethods.showToast(_context, "Selection not more than two");
            }

        } else {
            boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
            getChecked[mChildPosition] = isChecked;
            mChildCheckStates.put(mGroupPosition, getChecked);
        }

        for (Map.Entry<Integer, boolean[]> entry : mChildCheckStates.entrySet()) {
            // System.out.println(entry.getKey() + " : " + entry.getValue());
            //   CommonMethods.Log(TAG, entry.getKey() + ":" + entry.getValue());
            boolean[] value = entry.getValue();
            for (boolean b :
                    value) {
             //   CommonMethods.Log(TAG, entry.getKey() + ":" + b);
            }

        }


    }

    /*public void showCompareOptionsDialog(final Context context, String selectedOneValue, String optionTwoTitle) {

        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.compare_dialog);

        dialog.setCancelable(false);

        // set the custom dialog components - text

        TextView selectedOne = (TextView) dialog.findViewById(R.id.selectedOne);

        TextView selectedTwo = (TextView) dialog.findViewById(R.id.selectedTwo);
        Button dialogButtonCompare = (Button) dialog.findViewById(R.id.compare);
        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.cancel);


        //-----
        selectedOne.setText("" + selectedOneValue);
        selectedTwo.setText("" + selectedOneValue);
        //-----
        // if button is clicked, close the custom dialog
        dialogButtonCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        // if button is clicked, close the custom dialog
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }*/

    public void showCompareOptionsDialog(String selectedOneValue, String optionTwoTitle) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(_context);
        View mView = layoutInflaterAndroid.inflate(R.layout.compare_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(_context);
        alertDialogBuilderUserInput.setView(mView);

        TextView selectedOne = (TextView) mView.findViewById(R.id.selectedOne);
        selectedOne.setText("" + selectedOneValue);
        TextView selectedTwo = (TextView) mView.findViewById(R.id.selectedTwo);
        selectedTwo.setText("" + optionTwoTitle);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(_context.getString(R.string.compare), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
                    }
                })

                .setNegativeButton(_context.getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }


}