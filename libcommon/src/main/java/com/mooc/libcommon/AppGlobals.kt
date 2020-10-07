package com.mooc.libcommon

import android.app.Application
import java.lang.reflect.InvocationTargetException

class AppGlobals {
   companion object {

       private var sApplication: Application? = null

       fun getApplication(): Application {
           if (sApplication == null) {
               try {
                   sApplication = Class.forName("android.app.ActivityThread")
                       .getMethod("currentApplication")
                       .invoke(null) as Application
               } catch (e: IllegalAccessException) {
                   e.printStackTrace();
               } catch (e: InvocationTargetException) {
                   e.printStackTrace();
               } catch (e: NoSuchMethodException) {
                   e.printStackTrace();
               } catch (e: ClassNotFoundException) {
                   e.printStackTrace();
               }
           }

           return sApplication as Application
       }
   }
}
