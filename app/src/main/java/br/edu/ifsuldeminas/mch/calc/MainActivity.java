package br.edu.ifsuldeminas.mch.calc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ifsuldeminas.mch.calc";
    private TextView textViewResultado;

    private TextView textViewHistorico;
    private String expressaoAtual = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResultado = findViewById(R.id.textViewResultadoID);
        textViewHistorico = findViewById(R.id.history);

        configurarBotaoNumero(R.id.buttonZeroID, "0");
        configurarBotaoNumero(R.id.buttonUmID, "1");
        configurarBotaoNumero(R.id.buttonDoisID, "2");
        configurarBotaoNumero(R.id.buttonTresID, "3");
        configurarBotaoNumero(R.id.buttonQuatroID, "4");
        configurarBotaoNumero(R.id.buttonCincoID, "5");
        configurarBotaoNumero(R.id.buttonSeisID, "6");
        configurarBotaoNumero(R.id.buttonSeteID, "7");
        configurarBotaoNumero(R.id.buttonOitoID, "8");
        configurarBotaoNumero(R.id.buttonNoveID, "9");

        findViewById(R.id.buttonSomaID).setOnClickListener(v -> acrescentarExpressao("+"));
        findViewById(R.id.buttonSubtracaoID).setOnClickListener(v -> acrescentarExpressao("-"));
        findViewById(R.id.buttonMultiplicacaoID).setOnClickListener(v -> acrescentarExpressao("*"));
        findViewById(R.id.buttonDivisaoID).setOnClickListener(v -> acrescentarExpressao("/"));
        findViewById(R.id.buttonVirgulaID).setOnClickListener(v -> acrescentarExpressao("."));

        // (AC)
        findViewById(R.id.buttonResetID).setOnClickListener(v -> {
            expressaoAtual = "";
            textViewResultado.setText("0");
            textViewHistorico.setText("");
        });

        // (+/-)
        findViewById(R.id.buttonDeleteID).setOnClickListener(v -> {
            if (!expressaoAtual.isEmpty() && !expressaoAtual.equals("0")) {
                if (expressaoAtual.startsWith("-")) {
                    expressaoAtual = expressaoAtual.substring(1);
                } else {
                    expressaoAtual = "-" + expressaoAtual;
                } atualizarTela();
            }
        });

        // (%)
        findViewById(R.id.buttonPorcentoID).setOnClickListener(v -> {
            try {
                if (expressaoAtual.isEmpty()) return;
                Calculable avaliador = new ExpressionBuilder(expressaoAtual).build();
                double resultado = avaliador.calculate() / 100.0;
                expressaoAtual = String.valueOf(resultado);
                atualizarTela();
            } catch (Exception e) {
                textViewResultado.setText("Erro");
            }
        });

        // (=)
        findViewById(R.id.buttonIgualID).setOnClickListener(v -> {
            try {
                if (expressaoAtual.isEmpty()) return;

                String expressaoOriginal = expressaoAtual;

                Calculable avaliadorExpressao = new ExpressionBuilder(expressaoAtual).build();
                Double resultado = avaliadorExpressao.calculate();

                if (resultado == resultado.longValue()) {
                    expressaoAtual = String.valueOf(resultado.longValue());
                } else {
                    expressaoAtual = resultado.toString();
                }

                textViewHistorico.setText(getString(R.string.historico_final, expressaoOriginal, expressaoAtual));
                textViewResultado.setText(expressaoAtual);

            } catch (Exception e) {
                Log.d(TAG, e.getMessage() != null ? e.getMessage() : "Erro de sintaxe");
                textViewResultado.setText("Erro");
            }
        });
    }

    private void configurarBotaoNumero(int id, String numero) {
        findViewById(id).setOnClickListener(v -> acrescentarExpressao(numero));
    }

    private void acrescentarExpressao(String valor) {
        if (expressaoAtual.equals("0") && !valor.equals(".")) {
            expressaoAtual = "";
        }

        boolean isNovoValorOperador = valor.equals("+") || valor.equals("-") || valor.equals("*") || valor.equals("/");

        if (isNovoValorOperador && !expressaoAtual.isEmpty()) {
            char ultimoCaracter = expressaoAtual.charAt(expressaoAtual.length() - 1);
            boolean isUltimoOperador = ultimoCaracter == '+' || ultimoCaracter == '-' || ultimoCaracter == '*' || ultimoCaracter == '/';

            if (isUltimoOperador) {
                expressaoAtual = expressaoAtual.substring(0, expressaoAtual.length() - 1);
            }
        }

        if (expressaoAtual.isEmpty() && isNovoValorOperador && !valor.equals("-")) {
            return;
        }

        expressaoAtual += valor;
        atualizarTela();
    }

    private void atualizarTela() {
        String visual = expressaoAtual.replace("*", "×").replace("/", "÷");
        textViewResultado.setText(visual.isEmpty() ? "0" : visual);
    }
}