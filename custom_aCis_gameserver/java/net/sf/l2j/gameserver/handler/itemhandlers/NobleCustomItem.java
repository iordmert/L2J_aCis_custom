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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.L2Playable;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;

/**
 * @author Konstantinos
 *
 */
public class NobleCustomItem implements IItemHandler
{
	
	public NobleCustomItem()
	{
		// null
	}
	
	@Override
	public void useItem(L2Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof L2PcInstance))
			return;
		
		if (Config.NOBLE_CUSTOM_ITEMS)
		{
			if (!(playable instanceof L2PcInstance))
				return;
			
			L2PcInstance activeChar = (L2PcInstance) playable;
			
			if (activeChar.isInOlympiadMode())
			{
				activeChar.sendMessage("This Item Cannot Be Used On Olympiad Games.");
			}
			
			if (activeChar.isNoble())
			{
				activeChar.sendMessage("You Are Already A Noblesse!.");
			}
			else
			{
				//activeChar.broadcastPacket(new SocialAction(activeChar.getObjectId(), 16));
				activeChar.setNoble(true, true);
				activeChar.sendMessage("You Are Now a Noble,You Are Granted With Noblesse Status , And Noblesse Skills.");
				activeChar.broadcastUserInfo();
				playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
				activeChar.getInventory().addItem("Tiara", 7694, 1, activeChar, null);
			}
			activeChar = null;
		}
	}
	

}
