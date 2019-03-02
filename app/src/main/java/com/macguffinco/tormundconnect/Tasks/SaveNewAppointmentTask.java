package com.macguffinco.tormundconnect.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.macguffinco.tormundconnect.Logic.AppointmentManager;
import com.macguffinco.tormundconnect.fragments.AppointmentFragment;
import com.macguffinco.tormundconnect.fragments.NewAppointmentFragment;
import com.macguffinco.model.appointments.AppointmentDC;

public class SaveNewAppointmentTask extends AsyncTask<String, Integer, Long> {

    AppointmentDC _appointmentDC;
    private Context context;
    private NewAppointmentFragment fragment;
    private AppointmentFragment.OnListFragmentInteractionListener mListener;


    public SaveNewAppointmentTask(Context context, NewAppointmentFragment fragment,AppointmentDC appointmentDC){
        this.context = context;
        _appointmentDC=appointmentDC;
        this.fragment=fragment;

    }


    @Override
    protected Long doInBackground(String... strings) {
        _appointmentDC = AppointmentManager.CreateNewAppointment(_appointmentDC);
        return null;
    }

    @Override
    protected void onPostExecute(Long result) {


        if(_appointmentDC.Id>0){
            Toast.makeText(context, "Cita guardada correctamente", Toast.LENGTH_SHORT).show();
        }else if(_appointmentDC.Id<=0){
            Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show();
        }

        _appointmentDC.Customer=null;
        _appointmentDC.AppointmentDate=null;
        _appointmentDC.Branch=null;
        _appointmentDC.Employee=null;
        fragment.horarySpinner.setText("");
        fragment.spinnerService.setText("");
        fragment.spinnerCustomers.setText("");

        FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
        ft.detach(fragment).attach(fragment).commit();

    }


}
