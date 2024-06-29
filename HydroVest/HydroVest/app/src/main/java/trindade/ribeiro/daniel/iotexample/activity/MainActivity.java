package trindade.ribeiro.daniel.iotexample.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import trindade.ribeiro.daniel.iotexample.util.Config;
import trindade.ribeiro.daniel.iotexample.model.MainActivityViewModel;
import trindade.ribeiro.daniel.iotexample.R;

public class MainActivity extends AppCompatActivity {

    // Guarda o estado do LED
    boolean ledStatus = false;

    // Guarda o estado do motor
    boolean bombaStatus = false;

    //Guarda o estado do cooler
    boolean coolerStatus = false;

    // Guarda o viewmodel
    MainActivityViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // setamos a toolbar como a toolbar padrão da app
        Toolbar toolbar = findViewById(R.id.tbMain);
        setSupportActionBar(toolbar);

        // obtem o viewmodel correspondente. É através do viewmodel que é possível obter os
        // estados do LED e motor, acioná-los e configurá-los
        vm = new ViewModelProvider(this).get(MainActivityViewModel.class);

        // realiza consulta ao ESP32 para verificar se o LED está ligado ou desligado
        updateLedStatus();

        // realiza consulta ao ESP32 para verificar se o motor está ligado ou desligado
        updateBombaStatus();

        // realiza consulta ao ESP32 para verificar se o cooler está ligado ou desligado
        updateCoolerStatus();

        // realiza consulta ao ESP32 para verificar a temperatura
        updateTempStatus();

        // realiza consulta ao ESP32 para verificar a umidade
        updateUmidStatus();

        // tratamento do comportando do botão que liga e desliga o LED
        Button btnLed = findViewById(R.id.btnLed);
        btnLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quando o botão é clicado, ele é desabilitado. Isso garante que o usuário não
                // será capaz de clicar várias vezes no botão
                v.setEnabled(false);

                // guarda uma promessa de resposta por parte do ESP32. Assim que o ESP32 responder,
                // a variável abaixo guarda o resultado da ação: um booleano, onde true indica que
                // o ESP32 conseguiu realizar a ação e false caso contrário
                LiveData<Boolean> resLD;

                // Se o estado atual do botão está ligado, enviamos uma requisição ao ESP32 para
                // desligar. Caso contrário, enviamos uma requisição ao ESP32 para ligar
                if(ledStatus) {
                    resLD = vm.turnLedOff();
                }
                else {
                    resLD = vm.turnLedOn();
                }

                // depois de enviar a requisição ao ESP32, nós observamos a variável que resLD.
                // Assim que o ESP32 responder, o método onChanged abaixo é chamado e entrega a
                // resposta
                resLD.observe(MainActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {

                        // Independente se o ESP32 realizou a ação ou não, fazemos uma requisição
                        // ao ESP32 para saber o estado atual do LED
                        updateLedStatus();

                        // reabilitamos novamente o botão que permite ligar/desligar o LED
                        v.setEnabled(true);
                    }
                });
            }
        });

        // tratamento do comportando do botão que liga e desliga o motor
        Button btnMotor = findViewById(R.id.btnBomba);
        btnMotor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quando o botão é clicado, ele é desabilitado. Isso garante que o usuário não
                // será capaz de clicar várias vezes no botão
                v.setEnabled(false);

                // guarda uma promessa de resposta por parte do ESP32. Assim que o ESP32 responder,
                // a variável abaixo guarda o resultado da ação: um booleano, onde true indica que
                // o ESP32 conseguiu realizar a ação e false caso contrário
                LiveData<Boolean> resLD;

                // Se o estado atual do motor está ligado, enviamos uma requisição ao ESP32 para
                // desligar. Caso contrário, enviamos uma requisição ao ESP32 para ligar
                if(bombaStatus) {
                    resLD = vm.turnBombaOff();
                }
                else {
                    resLD = vm.turnBombaOn();
                }

                // depois de enviar a requisição ao ESP32, nós observamos a variável que resLD.
                // Assim que o ESP32 responder, o método onChanged abaixo é chamado e entrega a
                // resposta
                resLD.observe(MainActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {

                        // Independente se o ESP32 realizou a ação ou não, fazemos uma requisição
                        // ao ESP32 para saber o estado atual do LED
                        updateBombaStatus();

                        // reabilitamos novamente o botão que permite ligar/desligar o motor
                        v.setEnabled(true);
                    }
                });
            }
        });
        Button btnCooler = findViewById(R.id.btnCooler);
        btnCooler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quando o botão é clicado, ele é desabilitado. Isso garante que o usuário não
                // será capaz de clicar várias vezes no botão
                v.setEnabled(false);

                // guarda uma promessa de resposta por parte do ESP32. Assim que o ESP32 responder,
                // a variável abaixo guarda o resultado da ação: um booleano, onde true indica que
                // o ESP32 conseguiu realizar a ação e false caso contrário
                LiveData<Boolean> resLD;

                // Se o estado atual do motor está ligado, enviamos uma requisição ao ESP32 para
                // desligar. Caso contrário, enviamos uma requisição ao ESP32 para ligar
                if(coolerStatus) {
                    resLD = vm.turnCoolerOff();
                }
                else {
                    resLD = vm.turnCoolerOn();
                }

                // depois de enviar a requisição ao ESP32, nós observamos a variável que resLD.
                // Assim que o ESP32 responder, o método onChanged abaixo é chamado e entrega a
                // resposta
                resLD.observe(MainActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {

                        // Independente se o ESP32 realizou a ação ou não, fazemos uma requisição
                        // ao ESP32 para saber o estado atual do LED
                        updateCoolerStatus();

                        // reabilitamos novamente o botão que permite ligar/desligar o motor
                        v.setEnabled(true);
                    }
                });
            }
        });

        // obtem o textview que mostra a velocidade atual do motor
        /*TextView tvVelRes = findViewById(R.id.tvVelRes);

        // obtém o slider de velocidade
        SeekBar skMotorVelocity = findViewById(R.id.skMotorVelocity);
        skMotorVelocity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // esse método é chamado sempre que o usuário modifica o slider de velocidade. Sempre
            // que isso acontece, atualizamos o valor que é mostrado no textview que exibe a
            // velocidade
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvVelRes.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            // esse método é chamado assim que o usuário para de ajusta o slider
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // obtem o valor atual do slider
                int newVelocity  = seekBar.getProgress();

                // realiza uma requisição ao ESP32 para ajustar a velocidade do motor usando a
                // velocidade definida pelo usuário no slider. Observe que aqui a gente não depende
                // de saber se o motor conseguiu ou não ajustar a velocidade. Então não é
                // necessário observar o resultado e realizar alguma ação depois que o ESP32 responde
                vm.setMotorVel(newVelocity);

            }
        });*/
    }

    // preenche o menu da toolbar com os itens de ações definidos no xml main_activity_tb
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_tb, menu);
        return true;
    }

    // método que é chamado toda vez que um item da toolbar é chamado
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // caso o usuário clique na ação de configurar, exibimos uma caixa de diálogo que
        // permite que o usuário configure o endereço do ESP32
        if (item.getItemId() == R.id.opConfig) {

            // Constrói o layout da caixa de diálogo
            LayoutInflater inflater = getLayoutInflater();
            View configDlgView = inflater.inflate(R.layout.config_dlg, null);

            // obtem a caixa de texto dentro da caixa de diálogo usada pelo usuário
            EditText etESP32Address = configDlgView.findViewById(R.id.etESP32Address);

            // setamos na caixa de texto o valor atual de endereço do ESP32
            etESP32Address.setText(Config.getESP32Address(this));

            // Construção da caixa de diálogo
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // definimos o layout que a caixa de diálogo vai ter
            builder.setView(configDlgView);

            // definimos o que acontece quando clicamos no botão OK da caixa de diálogo
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // obtemos o endereço do ESP32 definido pelo usuário
                    String esp32Address = etESP32Address.getText().toString();

                    // salvamos esse novo endereço no arquivo de configuração da app
                    Config.setESP32Address(MainActivity.this, esp32Address);
                }
            });

            // se clicar em cancelar, não fazemos nada
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            // depois de configurada a caixa, a criamos e exibimos
            builder.create().show();

            return true;
        }

        // caso o usuário clique na ação de atualizar, realizamos requisições ao ESP32 para
        // obter o estado atual do LED e motor
        if (item.getItemId() == R.id.opUpdate) {
            updateLedStatus();
            updateBombaStatus();
            updateCoolerStatus();
            updateTempStatus();
            updateUmidStatus();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // realiza uma requisição ao ESP32 para verificar se o LED está ligado ou não
    void updateLedStatus() {

        // obtém o textview que exibe o status do motor
        TextView tvLedStatusRes = findViewById(R.id.tvLedStatusRes);

        // obtém o button que permite ligar/desligar o motor
        Button btnLed = findViewById(R.id.btnLed);

        // envia uma requisição ao ESP32 para saber se o motor está ligado ou desligado
        LiveData<Boolean> ledStatusLD = vm.getLedStatus();

        // observa motorStatusLD. Assim que o ESP32 responder, o resultado vai aparecer em motorStatusLD
        ledStatusLD.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                // guarda o estado atual do motor
                ledStatus = aBoolean;

                // se o motor está ligado, mudamos o valor de tvMotorStatusRes para ligado e mudamos
                // também o texto que aparece no botão para desligar
                if(aBoolean) {
                    tvLedStatusRes.setText("Ligado");
                    btnLed.setText("Desligar");
                }
                // se o motor está desligado, mudamos o valor de tvMotorStatusRes para desligado e mudamos
                // também o texto que aparece no botão para ligar
                else {
                    tvLedStatusRes.setText("Desligado");
                    btnLed.setText("Ligar");
                }
            }
        });
    }

    void updateBombaStatus() {

        // obtém o textview que exibe o status do motor
        TextView tvBombaStatusRes = findViewById(R.id.tvBombaStatusRes);

        // obtém o button que permite ligar/desligar o motor
        Button btnBomba = findViewById(R.id.btnBomba);

        // envia uma requisição ao ESP32 para saber se o motor está ligado ou desligado
        LiveData<Boolean> motorStatusLD = vm.getMotorStatus();

        // observa motorStatusLD. Assim que o ESP32 responder, o resultado vai aparecer em motorStatusLD
        motorStatusLD.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                // guarda o estado atual do motor
                bombaStatus = aBoolean;

                // se o motor está ligado, mudamos o valor de tvBombaStatusRes para ligado e mudamos
                // também o texto que aparece no botão para desligar
                if(aBoolean) {
                    tvBombaStatusRes.setText("Ligado");
                    btnBomba.setText("Desligar");
                }
                // se o motor está desligado, mudamos o valor de tvBombaStatusRes para desligado e mudamos
                // também o texto que aparece no botão para ligar
                else {
                    tvBombaStatusRes.setText("Desligado");
                    btnBomba.setText("Ligar");
                }
            }
        });
    }
    void updateCoolerStatus() {

        // obtém o textview que exibe o status do cooler
        TextView tvCoolerStatusRes = findViewById(R.id.tvCoolerStatusRes);

        // obtém o button que permite ligar/desligar o cooler
        Button btnCooler = findViewById(R.id.btnCooler);

        // envia uma requisição ao ESP32 para saber se o cooler está ligado ou desligado
        LiveData<Boolean> coolerStatusLD = vm.getCoolerStatus();

        // observa coolerStatusLD. Assim que o ESP32 responder, o resultado vai aparecer em coolerStatusLD
        coolerStatusLD.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                // guarda o estado atual do LED
                coolerStatus = aBoolean;

                // se o LED está ligado, mudamos o valor de tvLedStatusRes para ligado e mudamos
                // também o texto que aparece no botão para desligar
                if(aBoolean) {
                    tvCoolerStatusRes.setText("Ligado");
                    btnCooler.setText("Desligar");
                }
                // se o LED está desligado, mudamos o valor de tvLedStatusRes para desligado e mudamos
                // também o texto que aparece no botão para ligar
                else {
                    tvCoolerStatusRes.setText("Desligado");
                    btnCooler.setText("Ligar");
                }
            }
        });
    }
    void updateTempStatus() {

        // obtém o textview que exibe a umidade
        TextView tvTempRes = findViewById(R.id.tvTempRes);

        // envia uma requisição ao ESP32 para saber se o cooler está ligado ou desligado
        LiveData<String> tempStatusLD = vm.getTempStatus();

        // observa umidStatusLD. Assim que o ESP32 responder, o resultado vai aparecer em coolerStatusLD
        tempStatusLD.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvTempRes.setText(s);
            }
        });
    }
void updateUmidStatus() {

    // obtém o textview que exibe a umidade
    TextView tvUmidRes = findViewById(R.id.tvUmidRes);

    // envia uma requisição ao ESP32 para saber se o cooler está ligado ou desligado
    LiveData<String> umidStatusLD = vm.getUmidStatus();

    // observa umidStatusLD. Assim que o ESP32 responder, o resultado vai aparecer em coolerStatusLD
    umidStatusLD.observe(this, new Observer<String>() {
        @Override
        public void onChanged(String s) {
            tvUmidRes.setText(s);
        }
    });
}
}