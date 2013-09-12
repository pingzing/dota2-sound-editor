/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dota.sound.editor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 *
 * @author Image 17
 */
public class ScriptParser
{

    private TreeModel scriptTree = null;
    private int currentLevel = 0;
    private int previousLevel = 0;
    private DefaultMutableTreeNode currentNode = null;
    private DefaultMutableTreeNode currentParent = null;

    ScriptParser(File script)
    {
        //Create the root node
        scriptTree = new DefaultTreeModel(new DefaultMutableTreeNode("root"));
        currentNode = (DefaultMutableTreeNode) scriptTree.getRoot();
        currentParent = currentNode;

        InputStream fis;
        BufferedReader br;

        FileWriter fos;
        BufferedWriter bw;
        String line;

        try
        {
            fis = new FileInputStream(script);
            br = new BufferedReader(new InputStreamReader(fis));

            while ((line = br.readLine()) != null)
            {
                parseScript(line);
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            System.out.println("Done generating tree.");
        }
    }

    public TreeModel getTreeModel()
    {
        return this.scriptTree;
    }

    private void parseScript(String line)
    {
        System.out.println("Looking at: " + line);
        if (line.isEmpty() || line.equals("") || line.startsWith("//"))
        {
            return;
        }
        else if (line.contains("{"))
        {
            currentLevel++;
            currentParent = currentNode;
            return;
        }
        else if (line.contains("}"))
        {
            currentLevel--;
            currentParent = (DefaultMutableTreeNode) currentParent.getParent();
            if (currentParent == null)
            {
                currentParent = (DefaultMutableTreeNode) scriptTree.getRoot();
            }
            return;
        }
        else
        {
            try
            {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(line);
                currentNode = newNode;
                if (currentNode != null)
                {
                    currentParent.add(newNode);
                }
            }
            catch (NullPointerException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
