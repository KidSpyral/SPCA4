package com.example.spca4.Actvity;

public class ReviewDialogFactory implements DialogFactory {
    @Override
    public CustomDialogFragment createDialog() {
        return CustomDialogFragment.newInstance();
    }
}