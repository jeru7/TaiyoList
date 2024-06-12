package com.example.taiyomarket.fragments;

// imports
import static android.content.Intent.getIntent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.taiyomarket.R;
import com.example.taiyomarket.classes.Item;
import com.example.taiyomarket.database.DBHelper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;
import com.example.taiyomarket.classes.User;

public class AddListPage extends Fragment implements FragmentManager.OnBackStackChangedListener {

//    variable declarations
    EditText listNameInput;
    ImageButton addItemBtn;
    RelativeLayout addListContainer;
    LinearLayout listItems;
    Button createBtn;
    TextView emptyText, itemText;
    String userEmail;
    long userId;
    DBHelper db;
    private List<Item> itemList = new ArrayList<>();
    private long listId;
    private AddItemPage addItemFragment;
    private AddListPage addListFragment;

//    constructor
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_list_page, container, false);
//        initialization lang ulit ng mga ui elements para maging dynamic

        listNameInput = (EditText) view.findViewById(R.id.list_name_input);
        createBtn = (Button) view.findViewById(R.id.create_button);
        addItemBtn = (ImageButton) view.findViewById(R.id.add_item);
        addListContainer = (RelativeLayout) view.findViewById(R.id.add_list_container);
        listItems = (LinearLayout) view.findViewById(R.id.list_items);
        emptyText = (TextView) view.findViewById(R.id.empty_text);
        itemText = (TextView) view.findViewById(R.id.item_text);
        db = new DBHelper(requireContext());
        addItemFragment = new AddItemPage();

//        kunin yung binasa ng landing page using bundle kanina
        if (getArguments() != null) {
            userEmail = getArguments().getString("email");
        }

        attachButtonEvents(); // attach lang ng listeners sa mga buttons
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        fetch lang ng user item sa db using the email
        User user = db.getUser(userEmail);

        if (user != null) {
            userId = user.getId();
        }

//        display list na meron si user at suggestions lang
        displayList(itemList);
        displaySuggestions(view);
    }

//    display list na meron si user
    public void displayList(List<Item> list) {
//        remove all views para if ever na may madagdag or macall ulit eh parang nagrerefresh lang
        listItems.removeAllViews();

//        check muna kung may laman si list
        if(!list.isEmpty()) {
//            pag may laman papakita si "ITEM" then alisin si emptyText
            itemText.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
//            gumagawa ulit ng textview per item sa list then ipapasok sa listItems na view
            for(Item item : list) {
                TextView textView = new TextView(requireContext());
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setText(item.getItemName() + " x" + item.getQuantity());
                textView.setTextSize(16);
                textView.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.medium));
                listItems.addView(textView);
            }
//            pag walang laman emptyText papakita
        } else {
            itemText.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }
    }

    public void attachButtonEvents() {
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fetch lang nung value ng list name
                String finalListName = listNameInput.getText().toString();
//                current time muna for both since create list lang to
                String dateCreated = getCurrentDateTime();
                String lastUpdate = getCurrentDateTime();

//               gawa ng bagong instance ni db helper, requireContext para sa current instance netong addlistpage fragment
                DBHelper db = new DBHelper(requireContext());

//                checker lang kung may laman yung list name field
                if(!finalListName.isEmpty()) {
//                    pag meron, i-add sa database si list kasama nung mga ibang values
//                    since nagrereturn siya ng id ng list, gagamitin natin siya para makapag pasok tayo ng item kay current list
                    listId = db.addList(userId, finalListName, dateCreated, lastUpdate);

                    if(!addItemFragment.getItemList().isEmpty()) {
//                    kunin yung temporary list sa additempage
                        itemList = addItemFragment.getItemList();

//                    then using for of (ewan ko ano tawag dito sa java pero same functionality ata sila ng for of sa js) magaadd tayo PER item sa list natin (NOTE: hawak natin yung current id ng new list)
                        for (Item item : itemList) {
//                        PER iteration inaadd yung ISANG item
                            db.addItemToList(listId, item.getItemName(), item.getQuantity());
                        }
                    }
//                    simple toast to verify the user na created na yung list
                    Toast.makeText(requireContext(), finalListName + " created successfully", Toast.LENGTH_SHORT).show();
//                    clear lang ng temporary list ng items
                    itemList.clear();

//                    inform si landing page na nag add na tayo ng list together with items. (refer to onAddList() sa landingpage)
                    if (getActivity() instanceof OnAddListListener) {
                        ((OnAddListListener) getActivity()).onAddList();
                    }
//                    pag ka create, imitate lang yung back action ng back button para mattrigger yung handleBackPress sa landingpage (refer to handleBackPress sa landingpage)
                    requireActivity().getOnBackPressedDispatcher().onBackPressed();
                } else {
//                    pag walang laman yung input field for list name, do nothing then verify the user lang
                    Toast.makeText(getContext(), "Please add a name for your list", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                set lang ng AddItemPage
                addItemFragment.setItemList(itemList);

//              open lang yung layout ni additempage
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.switchable_view, addItemFragment);
                transaction.addToBackStack(null);
                transaction.commit();

//                close yung mga nakaopen para magmukang new activity, tinira lang yung nav bar sa ilalim
                addListContainer.setVisibility(View.GONE);
                createBtn.setVisibility(View.GONE);
            }
        });
    }

//    display suggestions, static lang muna hahaha
    private void displaySuggestions(View rootView) {
        String[] suggestionsArray = {"Fruits", "Vegetables", "Meal Plan", "Monday", "Tuesday", "Wednesday", "Thursday", "Saturday", "Sunday", "Tomorrow"};

//        inflater lang at hindi frame transactions, so bali layout lang need natin
        ViewGroup suggestionsList = rootView.findViewById(R.id.suggestions_list);
        LayoutInflater inflater = LayoutInflater.from(requireContext());

//        create lang ng textview na may value ng mga strings na nasa suggestionsArray
        for (String suggestion : suggestionsArray) {
            TextView textView = (TextView) inflater.inflate(R.layout.suggestion_item, suggestionsList, false);
            textView.setText(suggestion);
//            then per suggestion item, mag aadd tayo ng functions (pag ni-click mapupunta yung text value sa input field ng list name)
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String selectedSuggestion = ((TextView) v).getText().toString();
                    listNameInput.setText(selectedSuggestion);
                }
            });
//             add lang ng suggestion item sa suggestion_list linear layout
            suggestionsList.addView(textView);
        }
    }

//    get current date lang, since dalawa may need neto, ginawan ko na lang ng method
    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

//    implemented method for fragment manager, kumbaga listener lang sila kung si AddListPage ba yung nasa screen or si AddItemPage
    @Override
    public void onResume() {
        super.onResume();
        getChildFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getChildFragmentManager().removeOnBackStackChangedListener(this);
    }

// eto yung natotoggle (remove/add) depende sa state ng fragment
    @Override
    public void onBackStackChanged() {
        boolean addItemPageVisible = getChildFragmentManager().findFragmentById(R.id.switchable_view) instanceof AddItemPage;
//        check muna kung visible si additempage
        if (!addItemPageVisible) {
//            pag hindi, display na ng mga layout na needed for additempage
            itemList = addItemFragment.getItemList();
            displayList(itemList);
            addListContainer.setVisibility(View.VISIBLE);
            createBtn.setVisibility(View.VISIBLE);
        }
    }

//    interface na iimplement ni landingpage para ma-detect niya kung nag add na tayo ng list (refer to createBtn sa taas)
    public interface OnAddListListener {
        void onAddList();
    }
}