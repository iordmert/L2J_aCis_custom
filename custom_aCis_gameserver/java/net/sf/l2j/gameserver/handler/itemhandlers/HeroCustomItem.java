/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.handler.itemhandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.L2Playable;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;


/**
 * @author Konstantinos
 *
 */
public class HeroCustomItem implements IItemHandler
{
	
	public HeroCustomItem()
	{
		// null
	}
	
	
	String INSERT_DATA = "REPLACE INTO characters_custom_data (obj_Id, char_name, hero, noble, donator, hero_end_date) VALUES (?,?,?,?,?,?)";
	
	@Override
	public void useItem(L2Playable playable, ItemInstance item, boolean forceUse)
	{
		
		if (Config.HERO_CUSTOM_ITEMS)
		{
			if (!(playable instanceof L2PcInstance))
				return;
			
			L2PcInstance activeChar = (L2PcInstance) playable;
			
			if (activeChar.isInOlympiadMode())
			{
				activeChar.sendMessage("This Item Cannot Be Used On Olympiad Games.");
			}
			
			if (activeChar.isHero())
			{
				activeChar.sendMessage("You Are Already A Hero!.");
			}
			else
			{
				//activeChar.broadcastPacket(new SocialAction(activeChar.getObjectId(), 16));
				activeChar.setHero(true);
				updateDatabase(activeChar, Config.HERO_CUSTOM_HOURS * 60L * 60L * 1000L);
				activeChar.sendMessage("You Are Now a Hero,You Are Granted With Hero Status , Skills ,Aura.");
				activeChar.broadcastUserInfo();
				playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
				activeChar.getInventory().addItem("Wings", 6842, 1, activeChar, null);
			}
			activeChar = null;
		}
	}
	
	
	private void updateDatabase(final L2PcInstance player, final long heroTime)
	{
		Connection con = null;
		try
		{
			if (player == null)
				return;
			
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_DATA);
			
			stmt.setInt(1, player.getObjectId());
			stmt.setString(2, player.getName());
			stmt.setInt(3, 1);
			stmt.setInt(4, player.isNoble() ? 1 : 0);
			stmt.setInt(5, 0);
			stmt.setLong(6, heroTime == 0 ? 0 : System.currentTimeMillis() + heroTime);
			stmt.execute();
			stmt.close();
			stmt = null;
		}
		catch (final Exception e)
		{
			_log.log(Level.ALL, "Error", e.getMessage());
		}
		finally
		{
			//con.close();
			con = null;
		}
	}

	
}
