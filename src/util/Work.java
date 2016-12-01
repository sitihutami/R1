/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import model.Category;
import model.MainSettings;
import model.ParsedApplicationSettings;
import model.Recipe;
import model.RecipeIngredient;
import model.RecipeStep;
import model.RecipeSummary;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author ai
 */
public class Work {
    public static final java.io.File f=new java.io.File(System.getProperty("user.home")+"/recipes_settings.xml");

    public static void init() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        model.MainSettings m=new model.MainSettings();
        m.setLogoActive("logo_active");
        m.setLogoInActive("logo");
        ArrayList<Category>a=new ArrayList<Category>();
        a.add(new Category("MAKANAN", "main", new java.util.LinkedList<Recipe>()));
        a.add(new Category("MINUMAN", "appet", new java.util.LinkedList<Recipe>()));
        a.add(new Category("JAJANAN", "timesaver", new java.util.LinkedList<Recipe>()));
        simpan(new ParsedApplicationSettings(a, m));
    }

    public static void simpan(ParsedApplicationSettings s) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        Document d=berkase();
        org.w3c.dom.Element root=d.createElement("Settings"),m=d.createElement("MainSettings");
        m.appendChild(tulisan(d,"LogoActive",s.getMainSettings().getLogoActive()));
        m.appendChild(tulisan(d,"LogoInactive",s.getMainSettings().getLogoInActive()));
        root.appendChild(m);
        for(Category c:s.getCategories())root.appendChild(buatCategory(d,c));
        d.appendChild(root);
        save(d);
    }

    private static Document berkase() throws ParserConfigurationException, SAXException, IOException {
        if(f.exists())return javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
        return javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    }

    private static Node tulisan(Document d, String tag, String isi) {
        org.w3c.dom.Element e=d.createElement(tag);
        e.appendChild(d.createTextNode(isi));
        return e;
    }

    private static Node buatCategory(Document d, Category c) {
        org.w3c.dom.Element e=d.createElement("Category");
        e.setAttribute("name", c.getName());
        e.setAttribute("icon", c.getIcon());
        for(model.Recipe r:c.getRecipes())e.appendChild(buatRecipe(d,r));
        return e;
    }

    private static void save(Document d) throws TransformerException {
        javax.xml.transform.dom.DOMSource ds=new javax.xml.transform.dom.DOMSource(d);
        javax.xml.transform.stream.StreamResult sr=new javax.xml.transform.stream.StreamResult(f);
        javax.xml.transform.Transformer t=javax.xml.transform.TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        t.setOutputProperty(javax.xml.transform.OutputKeys.METHOD, "xml");
        t.setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "no");
        t.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
        t.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "utf-8");
        t.transform(ds, sr);
    }

    private static Node buatRecipe(Document d, Recipe r) {
        org.w3c.dom.Element e=d.createElement("RecipeInfo"),lbahan=d.createElement("RecipeIngredientsList"),lstep=d.createElement("RecipeStepsList");
        e.setAttribute("id", r.getId());
        e.appendChild(Work.tulisan(d, "RecipeTitle", r.getRecipeTitle()));
        e.appendChild(buatPictureList(d,r.getRecipePictureUriList()));
        e.appendChild(buatSUmmary(d,r.getRecipeSummary()));
        for(model.RecipeIngredient ri:r.getRecipeIngredients())lbahan.appendChild(buatBahan(d,ri));
        e.appendChild(lbahan);
        for(model.RecipeStep rs:r.getRecipeSteps())lstep.appendChild(buatStep(d,rs));
        e.appendChild(lstep);
        return e;
    }

    private static Node buatPictureList(Document d, ArrayList<String> l) {
        org.w3c.dom.Element e=d.createElement("RecipePictureList");
        for(String s:l)e.appendChild(Work.tulisan(d, "RecipePicture", s));
        return e;
    }

    private static Node buatSUmmary(Document d, RecipeSummary rs) {
        org.w3c.dom.Element e=d.createElement("RecipeSummary");
        e.appendChild(Work.tulisan(d, "RecipeSummaryOrigin", rs.getRecipeSummaryOrigin()));
        e.appendChild(Work.tulisan(d, "RecipeSummaryPreparationTime", rs.getRecipeSummaryPreparationTime()));
        e.appendChild(Work.tulisan(d, "RecipeSummaryCookingTime", rs.getRecipeSummaryCookingTime()));
        e.appendChild(Work.tulisan(d, "RecipeSummaryPortions", rs.getRecipeSummaryPortions()));
        e.appendChild(Work.tulisan(d, "RecipeSummaryCalories", rs.getRecipeSummaryCalories()));
        e.appendChild(Work.tulisan(d, "RecipeSummaryDescription", rs.getRecipeSummaryDescription()));
        return e;
    }

    private static Node buatBahan(Document d, RecipeIngredient ri) {
        org.w3c.dom.Element e=d.createElement("RecipeIngredient");
        e.appendChild(Work.tulisan(d, "RecipeIngredientsName", ri.getRecipeIngredientName()));
        e.appendChild(Work.tulisan(d, "RecipeIngredientsQuantity", ri.getRecipeIngredientQuantity()));
        return e;
    }

    private static Node buatStep(Document d, RecipeStep rs) {
        org.w3c.dom.Element e=d.createElement("RecipeStep");
        e.appendChild(Work.tulisan(d, "RecipeStepName", rs.getRecipeStepName()));
        e.appendChild(Work.tulisan(d, "RecipeStepDescription", rs.getRecipeStepDescription()));
        return e;
    }

    public static ParsedApplicationSettings getData() throws ParserConfigurationException, SAXException, IOException {
        model.MainSettings m=getMainSettting();
        ArrayList<Category>a=getAllCategory();
        return new ParsedApplicationSettings(a, m);
    }

    private static MainSettings getMainSettting() throws ParserConfigurationException, SAXException, IOException {
        MainSettings m=new MainSettings();
        Document d=Work.berkase();
        org.w3c.dom.Element e1=(org.w3c.dom.Element) d.getElementsByTagName("LogoActive").item(0),
                e2=(org.w3c.dom.Element) d.getElementsByTagName("LogoInactive").item(0);
        m.setLogoActive(e1.getTextContent());
        m.setLogoInActive(e2.getTextContent());
        return m;
    }

    private static ArrayList<Category> getAllCategory() throws ParserConfigurationException, SAXException, IOException {
        ArrayList<Category>a=new ArrayList<Category>();
        Document d=Work.berkase();
        org.w3c.dom.NodeList nl=d.getElementsByTagName("Category");
        for(int x=0;x<nl.getLength();x++)if(nl.item(x).getNodeType()==Node.ELEMENT_NODE)a.add(getCategory(nl.item(x)));
        return a;
    }

    private static Category getCategory(Node i) {
        org.w3c.dom.Element e=(org.w3c.dom.Element) i;
        String name=e.getAttribute("name"),icon=e.getAttribute("icon");
        ArrayList<Recipe>a=new ArrayList<Recipe>();
        org.w3c.dom.NodeList nl=e.getElementsByTagName("RecipeInfo");
        for(int x=0;x<nl.getLength();x++)if(nl.item(x).getNodeType()==Node.ELEMENT_NODE)a.add(getRecipe(nl.item(x)));
        return new Category(name, icon, a);
    }

    private static Recipe getRecipe(Node i) {
        org.w3c.dom.Element e=(org.w3c.dom.Element) i;
        String id=e.getAttribute("id"),title=e.getElementsByTagName("RecipeTitle").item(0).getTextContent();
        ArrayList<String>pic=getPicture(e.getElementsByTagName("RecipePictureList").item(0));
        RecipeSummary rs=getRecipeSummary(e.getElementsByTagName("RecipeSummary").item(0));
        ArrayList<RecipeIngredient>a1=getBahan(e.getElementsByTagName("RecipeIngredientsList").item(0));
        ArrayList<model.RecipeStep>a2=getStep(e.getElementsByTagName("RecipeStepsList").item(0));
        return new Recipe(id, title, pic, rs, a1, a2);
    }

    private static RecipeSummary getRecipeSummary(Node i) {
        org.w3c.dom.Element e=(org.w3c.dom.Element) i;
        String origin=e.getElementsByTagName("RecipeSummaryOrigin").item(0).getTextContent(),
                prepare=e.getElementsByTagName("RecipeSummaryPreparationTime").item(0).getTextContent(),
                time=e.getElementsByTagName("RecipeSummaryCookingTime").item(0).getTextContent(),
                porsi=e.getElementsByTagName("RecipeSummaryPortions").item(0).getTextContent(),
                kkal=e.getElementsByTagName("RecipeSummaryCalories").item(0).getTextContent(),
                desc=e.getElementsByTagName("RecipeSummaryDescription").item(0).getTextContent();
        return new RecipeSummary(origin, prepare, time, porsi, kkal, desc);
    }

    private static ArrayList<String> getPicture(Node i) {
        ArrayList<String>a=new ArrayList<String>();
        org.w3c.dom.Element e=(org.w3c.dom.Element) i;
        org.w3c.dom.NodeList nl=e.getElementsByTagName("RecipePicture");
        for(int x=0;x<nl.getLength();x++)if(nl.item(x).getNodeType()==Node.ELEMENT_NODE)
            a.add(nl.item(x).getTextContent());
        return a;
    }

    private static ArrayList<RecipeIngredient> getBahan(Node i) {
        ArrayList<RecipeIngredient>a=new ArrayList<RecipeIngredient>();
        org.w3c.dom.Element e=(org.w3c.dom.Element) i;
        org.w3c.dom.NodeList nl=e.getElementsByTagName("RecipeIngredient");
        for(int x=0;x<nl.getLength();x++)if(nl.item(x).getNodeType()==Node.ELEMENT_NODE)a.add(getBahane(nl.item(x)));
        return a;
    }

    private static ArrayList<RecipeStep> getStep(Node i) {
        ArrayList<RecipeStep>a=new ArrayList<RecipeStep>();
        org.w3c.dom.Element e=(org.w3c.dom.Element) i;
        org.w3c.dom.NodeList nl=e.getElementsByTagName("RecipeStep");
        for(int x=0;x<nl.getLength();x++)if(nl.item(x).getNodeType()==Node.ELEMENT_NODE)a.add(getStepe(nl.item(x)));
        return a;
    }

    private static RecipeIngredient getBahane(Node i) {
        org.w3c.dom.Element e=(org.w3c.dom.Element) i;
        String name=e.getElementsByTagName("RecipeIngredientsName").item(0).getTextContent(),qty=
                e.getElementsByTagName("RecipeIngredientsQuantity").item(0).getTextContent();
        return new RecipeIngredient(name, qty);
    }

    private static RecipeStep getStepe(Node i) {
        org.w3c.dom.Element e=(org.w3c.dom.Element) i;
        String name=e.getElementsByTagName("RecipeStepName").item(0).getTextContent(),
                desc=e.getElementsByTagName("RecipeStepDescription").item(0).getTextContent();
        return new RecipeStep(name, desc);
    }

    public static int getCountRecipe() throws ParserConfigurationException, SAXException, IOException {
        Document d=Work.berkase();
        return d.getElementsByTagName("RecipeInfo").getLength();
    }

    public static void addRecipe(String jenis, Recipe r) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        ParsedApplicationSettings s=Work.getData();
        ArrayList<Category>a=s.getCategories();
        for(int x=0;x<a.size();x++){
            Category c=a.get(x);
            if(jenis == null ? c.getName() == null : jenis.equals(c.getName())){
                java.util.List<Recipe>l=c.getRecipes();
                l.add(r);
                c.setRecipes(l);
                a.set(x, c);
                s.setmCategories(a);
                break;
            }
        }f.delete();
        Work.simpan(s);
    }
}