//library used: https://github.com/Gikkman/Java-Twirk
import com.gikk.twirk.Twirk;
import com.gikk.twirk.TwirkBuilder;
import com.gikk.twirk.commands.PatternCommandExample;
import com.gikk.twirk.commands.PrefixCommandExample;
import com.gikk.twirk.events.TwirkListener;
import com.gikk.twirk.types.twitchMessage.TwitchMessage;
import com.gikk.twirk.types.users.TwitchUser;
import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("type the channel you want to join");
        Scanner scanner = new Scanner(new InputStreamReader(System.in, "UTF-8"));
        String channel = "#" + scanner.nextLine();
        final String exitMessage = "qwsxzad";

        final Twirk twirk = new TwirkBuilder(channel, "realhumanshark", "oauth:vyat2lub7izswefl4ysv4df8o9dce5")
                .setVerboseMode(true)    //We want to print everything we receive from Twitch
                .build();                //Create the Twirk object

        twirk.addIrcListener(getOnDisconnectListener(twirk));
        twirk.addIrcListener(new PatternCommandExample(twirk));
        twirk.addIrcListener(new PrefixCommandExample(twirk));

        twirk.connect();    //Connect to Twitch

        twirk.addIrcListener(new TwirkListener() {
            public void onPrivMsg(TwitchUser sender, TwitchMessage message) {
                String[] splitted = message.getContent().split(" ");
                Random rand = new Random();

                if (message.toString().equals(exitMessage)) {
                    twirk.disconnect();
                }

                if (message.toString().contains("howdy")) {
                    twirk.channelMessage("https://i.imgur.com/74l8A6F.png");
                }

                if (message.toString().contains("retard")) {
                    twirk.channelMessage("@".concat(sender.getUserName()).concat(" you cant say that"));
                    twirk.channelMessage("/timeout ".concat(sender.getUserName()).concat(" 1"));
                }

                if (splitted[0].charAt(0) == '!') {
                    if (splitted[0].equals("!lurk")) {

                        twirk.channelMessage("i see u");

                    } else if (splitted[0].equals("!dragon")) {

                        twirk.channelMessage("Neck1");
                        twirk.channelMessage("Neck2");
                        twirk.channelMessage("Neck2");
                        twirk.channelMessage("Neck3");

                    } else if (splitted[0].equals("!twitter")) {

                        twirk.channelMessage("https://www.twitter.com/crafthai");

                    } else if (splitted[0].equals("!timemeout")) {

                        twirk.channelMessage("/timeout " + sender.toString() + " 120");

                    } else if (splitted[0].equals("!free")) {

                        twirk.channelMessage("please direct your attention to the top right and" +
                                "give me your twitch prime sub");

                    } else if(splitted[0].equals("!random")){

                        int num = rand.nextInt(Integer.parseInt(splitted[1]));
                        twirk.channelMessage(Integer.toString(num));

                    } else if(splitted[0].equals("!stupid")){
                        String sendThis = ("hey ");

                        for(int i = 0; i < splitted.length; i++){
                            sendThis = sendThis.concat(splitted[i]).concat(" ");
                        }

                        twirk.channelMessage(sendThis.concat(", ur stupid"));
                    } else if(splitted[0].equals("!openpack")){
                        PackSimulator open = new PackSimulator();
                        String set = "";

                        if(splitted.length == 1){
                            try {
                                open.openPack("chaos ");
                            } catch (FileNotFoundException e) {
                                //oops
                            }
                            twirk.channelMessage(open.toString());
                        } else {

                            for (int i = 1; i < splitted.length; i++) {
                                set = set + splitted[i] + " ";
                            }
                            try {
                                open.openPack(set);
                            } catch (FileNotFoundException e) {
                                //oops
                            }
                            twirk.channelMessage(open.toString());
                        }

                    } else if(splitted[0].equals("!mawio")){
                        twirk.channelMessage("https://i.imgur.com/zg5XjCA.png");
                    }
                }
            }
        });
    }

    private static TwirkListener getOnDisconnectListener(final Twirk twirk) {

        return new TwirkListener() {
            @Override
            public void onDisconnect() {
                //Twitch might sometimes disconnects us from chat. If so, try to reconnect.
                try {
                    if (!twirk.connect())
                        //Reconnecting might fail, for some reason. If so, close the connection and release resources.
                        twirk.close();
                } catch (IOException e) {
                    //If reconnection threw an IO exception, close the connection and release resources.
                    twirk.close();
                } catch (InterruptedException e) { //1111 }
                }
            }

            ;
        };
    }
}
