package states;

import constants.Constants;
import graphics.Assets;
import graphics.Text;
import input.KeyBoard;
import input.Timer;
import io.ScoreData;
import main.Window;
import math.Vector2D;
import network.Server;
import observer.Observador;
import ui.Action;
import ui.Button;
import ui.ButtonTimed;
import ui.TextInput;

import javax.swing.*;
import java.awt.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Selector de nombre de la clase
 */
public class NameState extends State {

    private final ScoreData name;
    private Button acceptButton = null;
    private final ButtonTimed ipButton;
    private final ButtonTimed hostButton;
    private TextInput textInput;
    private boolean multiplayer;
    private boolean host = false;
    private boolean showAcceptButton;
    private String ipAddress = "localhost";

    public NameState(boolean multiplayer) {
        this.multiplayer = multiplayer;
        showAcceptButton = !multiplayer;

        name = new ScoreData(0,"AAAA");

        // text input
        textInput = new TextInput(4,name.getName());

        // boton para setear ip
        ipButton = new ButtonTimed(
                Assets.greyBtn,
                Assets.blueBtn,
                Constants.WIDTH / 2 - Assets.greyBtn.getWidth() / 2,
                Constants.HEIGHT / 2 + Assets.greyBtn.getHeight(),
                "Set Ip",
                (params) -> {
                    if (showAcceptButton) return;
                    ipAddress = JOptionPane.showInputDialog(null, "Enter HOST_IP");
                    showAcceptButton = true;
                    Button button = (Button) params[0];
                    button.setSelected(false);
                    if (acceptButton != null) {
                        acceptButton.setSelected(true);
                    }
                }
        );

        // boton de host
        hostButton = new ButtonTimed(
                Assets.greyBtn,
                Assets.blueBtn,
                Constants.WIDTH - 250,
                80,
                host ? "Host: Yes" : "Host: No",
                (params) -> {
                    if (!multiplayer) return;
                    Button button = (Button) params[0];
                    button.setText(host ? "Host: No" : "Host: Yes");
                    host = !host;
                    if (host && acceptButton != null) {
                        ipButton.setSelected(false);
                        acceptButton.setSelected(true);
                        showAcceptButton = true;
                    } else if (!host && acceptButton != null) {
                        ipButton.setSelected(true);
                        acceptButton.setSelected(false);
                        showAcceptButton = false;
                    }
                });

        // boton de aceptar
        acceptButton = new Button(
                Assets.greyBtn,
                Assets.blueBtn,
                Constants.WIDTH / 2 - Assets.greyBtn.getWidth() / 2,
                Constants.HEIGHT / 2 + 3*Assets.greyBtn.getHeight(),
                Constants.ACCEPT,
                (params) -> {
                    if (!showAcceptButton) return;
                    name.setName(textInput.getInput());

                    // close resources
                    hostButton.close();
                    textInput.close();
                    ipButton.close();

                    if (!multiplayer){
                        State.changeState(new GameState(this.name.getName()));
                        return;
                    }

                    // iniciar multiplayer server
                    String localIp = "";
                    try {
                        localIp = Inet4Address.getLocalHost().getHostAddress();
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    }
                    if (host) ipAddress = localIp == "" ? "localhost" : localIp;
                    Server server = new Server(this.name.getName(),ipAddress,host);

                    State.changeState(new MultiplayerState(this.name.getName(),server));
                }
        );


        if (!multiplayer) {
            acceptButton.setSelected(true);
        }
        else {
            ipButton.setSelected(true);
        }

    }

    /**
     * Actualiza el Estado
     *
     * @param dt tiempo entre frames en ms
     */
    @Override
    public void update(float dt) {
        textInput.update(dt);
        acceptButton.update();

        // verificar botones
        if(multiplayer && KeyBoard.HOST && Window.keyBoardTimer.isTime()){
            Window.keyBoardTimer.reset();
            hostButton.doAction();
        }
    }

    @Override
    public void draw(Graphics g) {

        g.drawImage(Assets.fondoName,0,0,Constants.WIDTH,Constants.HEIGHT,null);

        if (showAcceptButton) acceptButton.draw(g);

        if (multiplayer) {
            hostButton.draw(g);
            if (!showAcceptButton) ipButton.draw(g);
        }

        Graphics2D g2d = (Graphics2D) g;

        Text.drawText(g2d, "INTRODUCE NAME...", new Vector2D((double) Constants.WIDTH / 2, (double) Constants.HEIGHT / 2 - 50),
                true, Color.WHITE, Assets.fontBig);

        textInput.drawInput(g);

        if (host) {
            String localIp = "";
            try {
//                System.out.println(Inet4Address.getLocalHost().getHostAddress());
                localIp = Inet4Address.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
            Text.drawText(g2d, "Connect to " + localIp, new Vector2D((double) Constants.WIDTH / 2, (double) Constants.HEIGHT / 2 + 100),
                    true, Color.GREEN, Assets.fontMed);
        }

    }
}
