package com.scorg.dms.adapters;

import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.model.responsemodel.showsearchresultresponsemodel.PatientFileData;
import com.scorg.dms.model.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.ui.activities.FileTypeViewerActivity;
import com.scorg.dms.ui.activities.PatientList;
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
    List<SearchResult> searchResultForPatientDetails;

    // Hashmap for keeping track of our checkbox check states
    private HashMap<Integer, boolean[]> mChildCheckStates;
    private String mCheckedBoxGroupName = null;

    public PatientExpandableListAdapter(Context context, List<SearchResult> searchResult) {
        this._context = context;

        List<String> listDataHeader = new ArrayList<>();
        HashMap<String, ArrayList<PatientFileData>> listChildData = new HashMap<String, ArrayList<PatientFileData>>();

        for (SearchResult dataObject :
                searchResult) {
            String patientName = dataObject.getPatientName();
            String patientAddress = dataObject.getPatientAddress();
            String id = dataObject.getPatientId();
            listDataHeader.add(patientName);


            //--------
            // This is done to set getPatientId in child (PatientFileData)
            List<PatientFileData> patientFileData = dataObject.getPatientFileData();
            for (PatientFileData temp :
                    patientFileData) {
                temp.setRespectiveParentPatientID(id);
            }
            //--------

            listChildData.put(patientName, new ArrayList<PatientFileData>(patientFileData));
        }

        this.searchResultForPatientDetails = searchResult;
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
    public View getChildView(final int groupPosition, final int childPosition,
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

        final PatientFileData childElement = getChild(groupPosition, childPosition);

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

        //----------------
        if (isLastChild) {
            childViewHolder.mDividerLineMore.setVisibility(View.VISIBLE);
        } else {
            childViewHolder.mDividerLineMore.setVisibility(View.INVISIBLE);
        }
        //-----------------
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

        childViewHolder.rowLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------------------
                Intent intent = new Intent(_context, FileTypeViewerActivity.class);

                Bundle extra = new Bundle();

                ArrayList<PatientFileData> dataToSend = new ArrayList<PatientFileData>();
                dataToSend.add(childElement);

                SearchResult searchPatientInformation = searchPatientInfo(childElement.getRespectiveParentPatientID());
                extra.putSerializable(_context.getString(R.string.compare), dataToSend);

                extra.putString(DmsConstants.PATIENT_ADDRESS, searchPatientInformation.getPatientAddress());
                extra.putString(DmsConstants.DOCTOR_NAME, searchPatientInformation.getDoctorName());
                extra.putString(DmsConstants.ID, childElement.getRespectiveParentPatientID());
                extra.putString(DmsConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, "" + getGroup(mGroupPosition));
                intent.putExtra(DmsConstants.DATA, extra);

                _context.startActivity(intent);
                //-------------------
            }
        });

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

        @BindView(R.id.rowLay)
        LinearLayout rowLay;
        @BindView(R.id.dividerLineMore)
        View mDividerLineMore;
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

        //------------------
        int counterToCheckValues = 0;

        // To make arrayList of elements
        ArrayList<Boolean> tempStatusList = new ArrayList<>();
        for (Map.Entry<Integer, boolean[]> entries : mChildCheckStates.entrySet()) {
            boolean[] value = (boolean[]) entries.getValue();
            for (boolean tempData :
                    value) {
                tempStatusList.add(tempData);
            }
        }

        // Increment counter if value ==> false
        for (Boolean dataValue :
                tempStatusList) {
            if (!dataValue) {
                counterToCheckValues = counterToCheckValues + 1;
            }
        }

        // This is done to check that, all elements in list is ==>false then reset mCheckedBoxGroupName
        if (counterToCheckValues == tempStatusList.size()) {
            mCheckedBoxGroupName = null;
        }

        //------------------

        if (isChecked) {
            //----
            boolean flag = true; // TO handle operation when selected for same group.
            String tempName = (String) getGroup(mGroupPosition);
            if (mCheckedBoxGroupName == null) {
                mCheckedBoxGroupName = tempName;
                flag = true;
            } else if (!tempName.equalsIgnoreCase(mCheckedBoxGroupName)) {
                CommonMethods.showToast(_context, _context.getString(R.string.error_compare_patient));
                flag = false;
            }

            if (flag) {
                //------
                boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                getChecked[mChildPosition] = isChecked;
                mChildCheckStates.put(mGroupPosition, getChecked);
                //------
                ArrayList<Integer> tempCheckedDataToCompare = new ArrayList<>();
                boolean[] checkedValues = mChildCheckStates.get(mGroupPosition);

                for (int i = 0; i < checkedValues.length; i++) {
                    boolean checkedValue = checkedValues[i];
                    if (checkedValue == true) {
                        tempCheckedDataToCompare.add(i);
                    }
                }
                //------
                if (tempCheckedDataToCompare.size() == 1) {
                    PatientFileData child = getChild(mGroupPosition, tempCheckedDataToCompare.get(0));
                    showCompareOptionsDialog(child, null, mCheckedBoxGroupName, tempName);
                } else if (tempCheckedDataToCompare.size() == 2) {
                    PatientFileData child = getChild(mGroupPosition, tempCheckedDataToCompare.get(0));
                    PatientFileData child_1 = getChild(mGroupPosition, tempCheckedDataToCompare.get(1));
                    showCompareOptionsDialog(child, child_1, mCheckedBoxGroupName, tempName);
                } else {
                    getChecked[mChildPosition] = false;
                    mChildCheckStates.put(mGroupPosition, getChecked);
                    CommonMethods.showToast(_context, _context.getString(R.string.error_max_two_reports));
                }
            }
            this.notifyDataSetChanged();

        } else {
            boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
            getChecked[mChildPosition] = isChecked;
            mChildCheckStates.put(mGroupPosition, getChecked);
        }
    }

    public void showCompareOptionsDialog(final PatientFileData selectedOneValue_1, final PatientFileData selectedTwoValue_2, final String title, final String patientName) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(_context);
        final View mView = layoutInflaterAndroid.inflate(R.layout.compare_dialog, null);
        final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(_context);
        alertDialogBuilderUserInput.setView(mView);
        final AlertDialog showAlert = alertDialogBuilderUserInput.show();

        alertDialogBuilderUserInput.setTitle(title);

        TextView selectedOne = (TextView) mView.findViewById(R.id.selectedOne);
        Button mCancel = (Button) mView.findViewById(R.id.cancel);
        Button mCompare = (Button)mView.findViewById(R.id.compare);
        TextView mTitle = (TextView)mView.findViewById(R.id.title);
        TextView mFiletypeOne = (TextView)mView.findViewById(R.id.fileTypeOne);
        TextView mFiletypeTwo = (TextView)mView.findViewById(R.id.fileTypeTwo);

        selectedOne.setText("" + selectedOneValue_1.getReferenceId());
        mFiletypeOne.setText(""+selectedOneValue_1.getFileType());
        //----------
        TextView selectedTwo = (TextView) mView.findViewById(R.id.selectedTwo);
        if (selectedTwoValue_2 == null) {
            selectedTwo.setVisibility(View.GONE);
            mFiletypeTwo.setVisibility(View.GONE);
            mTitle.setText(_context.getString(R.string.error_select_second_file_type) + "\n\n" + title);
            mCompare.setBackgroundColor(Color.alpha(R.color.dialog_compare));
            mCompare.setEnabled(false);
        }
        else {
            selectedTwo.setVisibility(View.VISIBLE);
            mFiletypeTwo.setVisibility(View.VISIBLE);
            selectedTwo.setText("" + selectedTwoValue_2.getReferenceId());
            mFiletypeTwo.setText(""+selectedTwoValue_2.getFileType());
            mTitle.setText(title);
        }

        mCompare.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (selectedTwoValue_2 == null) {
                   alertDialogBuilderUserInput.setTitle(title+"\n"+_context.getString(R.string.error_select_second_file_type));
                            /*CommonMethods.showToast(_context, _context.getString(R.string.error_select_second_file_type));*/
               } else {
                   //
                   Intent intent = new Intent(_context, FileTypeViewerActivity.class);

                   Bundle extra = new Bundle();

                   ArrayList<PatientFileData> dataToSend = new ArrayList<PatientFileData>();
                   dataToSend.add(selectedOneValue_1);
                   dataToSend.add(selectedTwoValue_2);
                   SearchResult searchPatientInformation = searchPatientInfo(selectedOneValue_1.getRespectiveParentPatientID());
                   extra.putSerializable(_context.getString(R.string.compare), dataToSend);
                   extra.putString(DmsConstants.PATIENT_ADDRESS, searchPatientInformation.getPatientAddress());
                   extra.putString(DmsConstants.DOCTOR_NAME, searchPatientInformation.getDoctorName());
                   extra.putString(DmsConstants.ID, selectedOneValue_1.getRespectiveParentPatientID());
                   extra.putString(DmsConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, patientName);
                   intent.putExtra(DmsConstants.DATA, extra);
                   _context.startActivity(intent);
               }

           }
       });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert.dismiss();
            }
        });
       /* alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(_context.getString(R.string.compare), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                        if (selectedTwoValue_2 == null) {
<<<<<<< HEAD
                            alertDialogBuilderUserInput.setTitle(title+"\n"+_context.getString(R.string.error_select_second_file_type));
                            *//*CommonMethods.showToast(_context, _context.getString(R.string.error_select_second_file_type));*//*
=======
                            alertDialogBuilderUserInput.setTitle(title + "\n" + _context.getString(R.string.error_select_second_file_type));
>>>>>>> a7f02e304b66ddf9afbbd2301a664f4c1768e066
                        } else {
                            //
                            Intent intent = new Intent(_context, FileTypeViewerActivity.class);

                            Bundle extra = new Bundle();

                            ArrayList<PatientFileData> dataToSend = new ArrayList<PatientFileData>();
                            dataToSend.add(selectedOneValue_1);
                            dataToSend.add(selectedTwoValue_2);
                            SearchResult searchPatientInformation = searchPatientInfo(selectedOneValue_1.getRespectiveParentPatientID());
                            extra.putSerializable(_context.getString(R.string.compare), dataToSend);
                            extra.putString(DmsConstants.PATIENT_ADDRESS, searchPatientInformation.getPatientAddress());
                            extra.putString(DmsConstants.DOCTOR_NAME, searchPatientInformation.getDoctorName());
                            extra.putString(DmsConstants.ID, selectedOneValue_1.getRespectiveParentPatientID());
                            extra.putString(DmsConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, patientName);
                            intent.putExtra(DmsConstants.DATA, extra);

                            _context.startActivity(intent);
                        }

                    }
                })

                .setNegativeButton(_context.getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
*/


    /*    AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
        if (selectedTwoValue_2 == null) {
            alertDialogAndroid.setTitle(_context.getString(R.string.error_select_second_file_type) + "\n" + title);
            alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        } else {
            alertDialogAndroid.setTitle(title);
            alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        }


*/
    }

    private SearchResult searchPatientInfo(String patientId) {
        SearchResult searchResult = null;
        for (SearchResult searchResultPatientInfo : searchResultForPatientDetails) {
            if (searchResultPatientInfo.getPatientId().equals(patientId)) {
                searchResult = searchResultPatientInfo;
            }
        }

        return searchResult;
    }

}