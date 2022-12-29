package com.dhh.demo;

import androidx.fragment.app.ListFragment;
import com.dhh.rxlife1.RxLife;

/**
 * Created by dhh on 2019/1/28.
 *
 * @author dhh
 */
public class dsfsd {
    @Override
    protected void finalize() throws Throwable {
        RxLife.with(new ListFragment()).bindOnDestroy();
    }
}
