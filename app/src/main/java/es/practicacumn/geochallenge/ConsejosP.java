package es.practicacumn.geochallenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import java.util.ArrayList;

import es.practicacumn.geochallenge.Fragmentos.Frag_Consejos;
import es.practicacumn.geochallenge.Model.Consejos.Consejos;

public class ConsejosP extends AppCompatActivity {
    private ArrayList<Consejos> consejosPrimerosauxilios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consejos_p);
        consejosPrimerosauxilios =new ArrayList<Consejos>();
        consejosPrimerosauxilios.add(new Consejos(1,"Evalúa la seguridad","Antes de acercarte a una persona herida, asegúrate de que la escena sea segura para ti y para la víctima. Haz una evaluación rápida del entorno y toma precauciones para evitar cualquier peligro adicional."));
        consejosPrimerosauxilios.add(new Consejos(2,"Llama a los servicios de emergencia","Si la situación lo requiere, llama a los servicios de emergencia o pide ayuda a alguien cercano. Es importante obtener ayuda médica profesional lo antes posible."));
        consejosPrimerosauxilios.add(new Consejos(3,"Mantén la calma"," Mantén la calma y tranquiliza a la persona herida. Tu tranquilidad puede ayudar a mantener a la víctima calmada y colaborativa."));
        consejosPrimerosauxilios.add(new Consejos(4,"Evalúa la condición de la víctima"," Haz una evaluación rápida de la condición de la persona herida. Comprueba su respiración, pulso y nivel de conciencia. Si la víctima no respira o no tiene pulso, inicia la RCP (reanimación cardiopulmonar) de inmediato si estás capacitado para hacerlo."));
        consejosPrimerosauxilios.add(new Consejos(5,"Controla las hemorragias","Si la persona está sangrando, aplica presión directa sobre la herida con un paño limpio o tu mano para controlar la hemorragia. Eleva la extremidad afectada si es posible."));
        consejosPrimerosauxilios.add(new Consejos(6,"Inmoviliza lesiones", "Si sospechas de una fractura o lesión en la columna vertebral, evita mover a la persona herida y trata de inmovilizar la zona afectada utilizando tablillas o cualquier objeto rígido disponible."));
        consejosPrimerosauxilios.add(new Consejos(7,"No remuevas objetos incrustados","Si la persona tiene un objeto incrustado en una herida, no lo retires. Deja que los profesionales de salud lo manejen para evitar mayores daños."));
        consejosPrimerosauxilios.add(new Consejos(8,"Protege a la persona del frío o calor","Mantén a la persona abrigada o protegida del calor, según sea necesario, para prevenir la hipotermia o la insolación."));
        consejosPrimerosauxilios.add(new Consejos(9,"Administra medicamentos o tratamientos de emergencia solo si estás capacitado" ,"No administres medicamentos o tratamientos de los que no estés seguro, a menos que estés capacitado para hacerlo. Puedes empeorar la situación si no tienes el conocimiento adecuado."));
        consejosPrimerosauxilios.add(new Consejos(10,"Registra la información relevante","Si es posible, registra la información relevante sobre la situación, como la hora de la lesión, los síntomas y cualquier otra información importante que pueda ser útil para los profesionales de salud."));

        Bundle bundle = new Bundle();
        bundle.putSerializable("consejos", consejosPrimerosauxilios);
        bundle.putInt("tipo",1);

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