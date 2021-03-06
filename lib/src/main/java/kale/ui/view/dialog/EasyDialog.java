package kale.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;

import lombok.AccessLevel;
import lombok.Setter;

/**
 * @author Kale
 * @date 2016/11/22
 */
public class EasyDialog extends BaseEasyDialog {

    private BuildParams mBuildParams;

    /**
     * 仅仅对于{@link BaseCustomDialog}的子类才有用
     */
    private boolean isBottomDialog = false;

    @Setter(AccessLevel.PUBLIC)
    private DialogInterface.OnClickListener positiveListener;

    @Setter(AccessLevel.PUBLIC)
    private DialogInterface.OnClickListener neutralListener;

    @Setter(AccessLevel.PUBLIC)
    private DialogInterface.OnClickListener negativeListener;

    @Setter(AccessLevel.PUBLIC)
    private DialogInterface.OnClickListener onClickListener;

    @Setter(AccessLevel.PUBLIC)
    private DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener;

    /**
     * use {@link Builder#build()}
     */
    public EasyDialog() {
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        bindAndSetViews(window != null ? window.getDecorView() : null);
    }

    protected void bindAndSetViews(@Nullable View root) {
    }

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mBuildParams = (BuildParams) bundle.getSerializable(KEY_BUILD_PARAMS);
            isBottomDialog = bundle.getBoolean(KEY_IS_BOTTOM_DIALOG, false);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        BuildParams p = mBuildParams;
        Builder builder = new Builder(getActivity());

        builder.setTitle(p.title)
                .setIcon(p.mIconId)
                .setMessage(p.message)
                .setPositiveButton(p.positiveText, positiveListener)
                .setNeutralButton(p.neutralText, neutralListener)
                .setNegativeButton(p.negativeText, negativeListener)
                .setItems(p.items, null);

        if (p.items != null) {
            if (p.isMultiChoice) {
                builder.setMultiChoiceItems(p.items, p.checkedItems, onMultiChoiceClickListener);
            } else if (p.isSingleChoice) {
                builder.setSingleChoiceItems(p.items, p.checkedItem, onClickListener);
            } else {
                builder.setItems(p.items, onClickListener);
            }
        }
        modifyOriginBuilder(builder);
        return builder.create();
    }

    @CallSuper
    protected void modifyOriginBuilder(Builder builder) {
    }

    public BuildParams getBuildParams() {
        return mBuildParams;
    }

    public boolean isBottomDialog() {
        return isBottomDialog;
    }

    public static class Builder extends BaseEasyDialog.Builder<Builder> {

        public Builder(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        protected EasyDialog createDialog() {
            return new EasyDialog();
        }

    }

}
