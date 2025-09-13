package com.example.thedigitalnotelibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdminUsersItemAdapter extends FirestoreRecyclerAdapter<User, AdminUsersItemAdapter.UserViewHolder> {
    Context context;

    public AdminUsersItemAdapter(@NonNull FirestoreRecyclerOptions<User> options, Context context) {
        //constructor class
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User user) {
        holder.usernameTextView.setText(user.username);
        String emailString = "Email: " + user.email;
        holder.emailTextView.setText(emailString);
        String passwordString = "Password: " + user.password;
        holder.passwordTextView.setText(passwordString);
        String referenceString = "UserID: " + user.reference;
        holder.refTextView.setText(referenceString);

    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_users_recycler_item,parent,false);
        return new UserViewHolder(view);
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        TextView usernameTextView,emailTextView,refTextView, passwordTextView;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.recycler_username_text_view);
            emailTextView = itemView.findViewById(R.id.recycler_email_text_view);
            refTextView = itemView.findViewById(R.id.recycler_userReference_text_view);
            passwordTextView = itemView.findViewById(R.id.recycler_password_text_view);
        }
    }
}


// EXTRA TEXT

//        holder.itemView.setOnClickListener((v)->{
//            Intent intent = new Intent(context,AdminUsersOptionsActivity.class);
//            intent.putExtra("email",adapterUser.email);
//            intent.putExtra("password",adapterUser.password);
////            String docId = this.getSnapshots().getSnapshot(position).getId();
////            intent.putExtra("docId",docId);
//            context.startActivity(intent);
//        });


//    void showMenu(){
//        View view;
//        menuBtn = view.findViewById(R.id.admin_users_menu_btn);
//        PopupMenu popupMenu  = new PopupMenu(this.context,UserViewHolder.men);
//        popupMenu.getMenu().add("Delete");
//        popupMenu.show();
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                if(menuItem.getTitle()=="Delete"){
//                    String ref = adapterUser.getReference();
//                    FirebaseAuth.getInstance().signOut();
//                    FirebaseAuth.getInstance().signInWithEmailAndPassword(adapterUser.email, adapterUser.password);
//                    FirebaseAuth.getInstance().getCurrentUser().delete();
//                    FirebaseAuth.getInstance().signOut();
//                    FirebaseAuth.getInstance().signInWithEmailAndPassword("exzitin@gmail.com", "0568928517Banana01!");
//                    return true;
//                }
//                return false;
//            }
//        });

//   }