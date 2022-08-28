package com.nashtech.rookies.AssetManagement.utils;

import com.nashtech.rookies.AssetManagement.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Component
public class MyDateUtils {

    private Calendar calendar = Calendar.getInstance();
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.TAIWAN);


    public Date getDate(String dateInString){
        try{
            return dateFormat.parse(dateInString);

        }catch (Exception e){
            throw new ResourceNotFoundException("Cannot find Date" + e.getMessage());
        }
    }

    public String getString(Date date){
        return dateFormat.format(date);
    }

    public boolean isWeekendDay(Date day){
        calendar.setTime(day);
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);

        return dayWeek==1 || dayWeek==7;
    }
}
