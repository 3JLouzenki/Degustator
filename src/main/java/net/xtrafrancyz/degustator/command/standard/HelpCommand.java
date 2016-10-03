package net.xtrafrancyz.degustator.command.standard;

import sx.blah.discord.handle.obj.IMessage;

import net.xtrafrancyz.degustator.command.Command;
import net.xtrafrancyz.degustator.command.CommandManager;
import net.xtrafrancyz.degustator.user.User;

import java.util.Map;

/**
 * @author xtrafrancyz
 */
public class HelpCommand extends Command {
    private final CommandManager commands;
    
    public HelpCommand(CommandManager commands) {
        super("help",
            "`!help` - список команд\n"
                + "`!help <команда>` - информация о команде"
        );
        this.commands = commands;
    }
    
    @Override
    public void onCommand(IMessage message, String[] args) throws Exception {
        User user = User.get(message.getAuthor());
        if (args.length != 0) {
            Command command = commands.getCommand(args[0]);
            if (command == null) {
                message.reply("нет такой команды");
                return;
            }
            if (!user.hasPerm(command.perm)) {
                message.reply("у вас недостаточно прав для просмотра помощи по этой команде...");
                return;
            }
            String msg;
            if (command.help != null)
                msg = command.help;
            else
                msg = "У команды !" + command.command + " нет описания :frowning2:";
            message.getChannel().sendMessage(msg);
            return;
        }
        String msg = "\nДоступные для вас команды: ```";
        boolean first = true;
        for (Map.Entry<String, Command> entry : commands.registered.entrySet()) {
            if (!user.hasPerm(entry.getValue().perm))
                continue;
            if (!first)
                msg += ", ";
            else
                first = false;
            msg += entry.getKey();
        }
        msg += "```";
        msg += "Напишите `!help <команда>` чтобы посмотреть описание команды";
        message.reply(msg);
    }
}
