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

@SuppressWarnings("ALL")
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

                //if something goes really wrong
                if (message.toString().equals(exitMessage)) {
                    twirk.disconnect();
                }

                //if the sender is self, ignore
                if (sender.toString().equals("realhumanshark")) {
                    return;
                }

                if (message.toString().contains("howdy")) {
                    twirk.channelMessage("https://i.imgur.com/74l8A6F.png");
                }

                if (message.toString().contains("retard")) {
                    twirk.channelMessage("@".concat(sender.getUserName()).concat(" you cant say that"));
                    twirk.channelMessage("/timeout ".concat(sender.getUserName()).concat(" 1"));
                }

                if (splitted[0].charAt(0) == '!') {
                    switch (splitted[0]) {
                        case "!lurk":

                            twirk.channelMessage("i see u");

                            break;
                        case "!dragon":

                            twirk.channelMessage("Neck1");
                            twirk.channelMessage("Neck2");
                            twirk.channelMessage("Neck2");
                            twirk.channelMessage("Neck3");

                            break;
                        case "!twitter":

                            twirk.channelMessage("https://www.twitter.com/crafthai");

                            break;
                        case "!timemeout":

                            twirk.channelMessage("/timeout " + sender.toString() + " 120");

                            break;
                        case "!free":

                            twirk.channelMessage("please direct your attention to the top right and" +
                                    "give me your twitch prime sub");

                            break;
                        case "!random":

                            int num = rand.nextInt(Integer.parseInt(splitted[1]));
                            twirk.channelMessage(Integer.toString(num));

                            break;
                        case "!stupid":
                            String sendThis = ("hey ");

                            for (String aSplitted : splitted) {
                                sendThis = sendThis.concat(aSplitted).concat(" ");
                            }

                            twirk.channelMessage(sendThis.concat(", ur stupid"));
                            break;
                        case "!openpack":
                            PackSimulator open = new PackSimulator();
                            String set = "";

                            if (splitted.length == 1) {
                                try {
                                    open.openPack("chaos ");
                                } catch (FileNotFoundException e) {
                                    //oops
                                }
                                twirk.channelMessage(open.toString());
                            } else {

                                for (int i = 1; i < splitted.length; i++) {
                                    set = set.concat(splitted[i] + " ");
                                }
                                try {
                                    open.openPack(set);
                                } catch (FileNotFoundException e) {
                                    //oops
                                }
                                twirk.channelMessage(open.toString());
                            }
                            break;
                        case "!mawio":
                            twirk.channelMessage("https://i.imgur.com/zg5XjCA.png");
                            break;
                        case "!duel":

                            int winner;
                            String victor = "";

                            if (splitted.length == 1) {
                                twirk.channelMessage("Format should be either !duel <who to duel> OR !duel <entity 1> vs <entity 2>");
                            }

                            if (splitted.length == 2) {
                                winner = rand.nextInt(2);
                                if (splitted[1].equals("raichu")) {
                                    twirk.channelMessage("Raichu always wins");
                                } else if (winner == 1) {
                                    twirk.channelMessage("Winner is... " + splitted[1]);
                                } else {
                                    twirk.channelMessage("Winner is... " + sender.getUserName());
                                }
                            }

                            if (splitted.length > 2) {
                                winner = rand.nextInt(2);
                                boolean raichuFound = false;
                                int index = 0;
                                for (int i = 0; i < splitted.length; i++) {

                                    if(splitted[i].equals("raichu")){
                                        raichuFound = true;
                                        break;
                                    }

                                    if (splitted[i].equals("vs")) {
                                        index = i;
                                    }
                                }

                                if(raichuFound){
                                    victor = victor.concat("Raichu always wins");
                                } else if (winner == 0) {
                                    for (int i = 1; i < index; i++) {
                                        victor = victor.concat( " " + splitted[i]);
                                    }
                                } else {
                                    for (int i = index + 1; i < splitted.length; i++) {
                                        victor = victor.concat( " " + splitted[i]);
                                    }
                                }
                                twirk.channelMessage("Winner is... " + victor);
                            }
                            break;
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
        };
    }
}
