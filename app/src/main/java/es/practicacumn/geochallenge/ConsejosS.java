package es.practicacumn.geochallenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import java.util.ArrayList;

import es.practicacumn.geochallenge.Fragmentos.Frag_Consejos;
import es.practicacumn.geochallenge.Model.Consejos.Consejos;

public class ConsejosS extends AppCompatActivity {
    private ArrayList<Consejos> consejosSupervivencia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consejos_s);
        consejosSupervivencia=new ArrayList<Consejos>();
        consejosSupervivencia.add(new Consejos(1,"Encuentra refugio","Busca un lugar seguro para protegerte del clima, animales o posibles peligros. Un refugio improvisado, como una cueva, una carpa o un refugio construido con materiales naturales, puede ser una opción."));
        consejosSupervivencia.add(new Consejos(2,"Encuentra agua potable","El agua es esencial para la supervivencia. Asegúrate de encontrar una fuente de agua potable, como ríos, arroyos o lagos, y purificarla antes de beberla. Puedes usar filtros de agua, hervirla o usar pastillas purificadoras."));
        consejosSupervivencia.add(new Consejos(3,"Encuentra comida","Aprende a identificar plantas y frutas comestibles, y a cazar o pescar si es posible. Ten cuidado con las plantas y animales venenosos, y asegúrate de tener conocimientos básicos de caza y pesca antes de intentarlo."));
        consejosSupervivencia.add(new Consejos(4,"Mantén el fuego","El fuego es una herramienta valiosa para la supervivencia, ya que te proporciona calor, luz y la posibilidad de cocinar alimentos. Aprende a encender y mantener un fuego de manera segura, y asegúrate de tener los recursos necesarios, como cerillas, encendedores o un kit de inicio de fuego."));
        consejosSupervivencia.add(new Consejos(5,"Mantén la calma y el sentido común","Mantén la mente clara y el sentido común en situaciones de emergencia. Evita el pánico y piensa con claridad para tomar decisiones informadas y sensatas."));
        consejosSupervivencia.add(new Consejos(6,"Haz un plan","Planifica tus acciones y haz un plan de acción para asegurar tu supervivencia. Considera factores como el terreno, el clima, los recursos disponibles y la seguridad al tomar decisiones."));
        consejosSupervivencia.add(new Consejos(7,"Conoce los primeros auxilios", "Aprende habilidades básicas de primeros auxilios, como el manejo de heridas, la reanimación cardiopulmonar (RCP) y la estabilización de fracturas. Esto te puede ayudar en caso de lesiones o emergencias médicas."));
        consejosSupervivencia.add(new Consejos(8,"Mantén tus pertenencias y recursos en buen estado","Asegúrate de cuidar tus pertenencias y recursos, como alimentos, agua, herramientas y equipo de supervivencia. Mantenlos en buen estado y úsalos de manera responsable para que te duren el mayor tiempo posible."));
        consejosSupervivencia.add(new Consejos(9,"Comunícate con otros","Si estás en una situación de supervivencia con otras personas, es importante mantener una comunicación clara y efectiva. Esto puede ayudar a coordinar esfuerzos y tomar decisiones informadas en grupo."));
        consejosSupervivencia.add(new Consejos(10,"Infórmate y prepárate","Aprende sobre el entorno en el que te encuentras y prepárate adecuadamente para la situación de supervivencia en la que te encuentras. Lleva contigo un equipo de supervivencia básico, como una brújula, un cuchillo, una linterna, un botiquín de primeros auxilios y comida y agua adicionales."));


        Bundle bundle = new Bundle();
        bundle.putSerializable("consejos",consejosSupervivencia);
        bundle.putInt("tipo",2);

        Frag_Consejos fragConsejos = new Frag_Consejos();
        fragConsejos.setArguments(bundle);

        // Crea una transacción de fragmentos
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //replace elimina el fragmento existente y agrega un nuevo fragmento
        fragmentTransaction.replace(R.id.containerConsejos, fragConsejos);
        fragmentTransaction.commit();
    }
}