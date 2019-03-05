# -*- coding: utf-8 -*
# !/usr/bin/python3
# On importe Tkinter
from tkinter import *
from tkinter import messagebox
from PIL import ImageTk, Image
import json
import os,sys
from time import *
import Class





path = "/home/pi/scene/"
items = []

def getName(path):
    
    files=os.listdir(path)
    return files

class ItemList:

    def __init__(self,path):
        try:
            json_data=open(path,'r')
            data_dict=json.load(json_data)
            self.nom=data_dict["nom"]
            self.duree=data_dict["duree"]
            self.navigation=data_dict["navigation"]
            self.etat=data_dict["etat"]
            self.date=data_dict["date"]
            self.data=data_dict["data"]
            json_data.close()
            
        except ValueError:
            messagebox.showinfo("Alerte", "échec d'ouverture du fichier"+path)


    def _get_nom(self):
        return self.nom
    def _get_navigation(self):
        return self.navigation
    def _get_etat(self):
        return self.etat
    def _get_date(self):
        return self.date
    def _get_data(self):
        return self.data

   
       
            

def load_object(path,FrameName):
    files=os.listdir(path)
    
    for file in files:
      it=ItemList(path+file)
      items.append(it)
    cpt=0
    bag="#D3D3D3"
    for i in items:
        globals()['SceneFrame%s'%cpt]=Frame(FrameName, width=250, height=50, borderwidth=1, background='black')
        globals()['SceneFrame%s'%cpt].pack(fill="x")
        globals()['champ_label%s'%cpt] = Label(globals()['SceneFrame%s'%cpt], text="Nom: "+i.nom+" | Durée: "+strftime('%M : %S',gmtime((i.duree)/1000))+ "\nSens: "+i.navigation+"\nEtat: "+i.etat+"\nDate: "+i.date, bg=bag)
        globals()['champ_label%s'%cpt].pack(fill="x")
        cpt+=1
        if "#D3D3D3" in bag:
            bag="white"
        elif "white" in bag:
            bag="#D3D3D3"
       
 
        
# On crée une fenêtre, racine de notre interface
fenetre = Tk()

#creation de la frame titre logo
LogoFrame = Frame(fenetre, width=500, height=50, borderwidth=1, background='orange')
LogoFrame.pack(fill="x")
#creation du canevas contenant le logo
canvas = Canvas(LogoFrame, width=50, height=50,background="orange")
img=Image.open("logosnsm.png")
Rimg=Image.open("reload.png")
Pimg=Image.open("play.jpg")
img=img.resize((50,50), Image.ANTIALIAS)
Rimg=Rimg.resize((50,50),Image.ANTIALIAS)
Pimg=Pimg.resize((50,50),Image.ANTIALIAS)
Rimgg=ImageTk.PhotoImage(Rimg)
Pimgg=ImageTk.PhotoImage(Pimg)
imgg=ImageTk.PhotoImage(img)
canvas.create_image(25,25,image=imgg)

canvas.pack(side="left")
# On crée un label (ligne de texte) 
# Note : le premier paramètre passé au constructeur de Label est notre
# interface racine
champ_label = Label(LogoFrame, text="SimulBoat",background="orange",font="ArialBlack")

# On affiche le label dans la fenêtre
champ_label.pack(side="right",fill="x")
MainFrame=Frame(fenetre, width=500, height=500, borderwidth=1, background='white')
MainFrame.pack(fill="both")
vsb = Scrollbar(MainFrame, orient=VERTICAL)
vsb.grid(row=0, column=1, sticky=N+S)
c = Canvas(MainFrame,width=250, height=500,background='white',yscrollcommand=vsb.set)
c.grid(row=0, column=0, sticky="news")
vsb.config(command=c.yview)
MainFrame.grid_rowconfigure(0, weight=1)
MainFrame.grid_columnconfigure(0, weight=1)

LeftFrame=Frame(c, width=250, height=500, borderwidth=1, background='white')
LeftFrame.pack(fill="both",side="left")
#RightFrame=Frame(c, width=250, height=500, borderwidth=1, background='red')
#RightFrame.pack(fill="both",side="right")
load_object(path,LeftFrame)
c.create_window(1, 1,  window=LeftFrame)
LeftFrame.update_idletasks()
c.config(scrollregion=c.bbox("all"))

#messagebox=tkMessageBox.FunctionName(title, message [, options])
def hello():
   messagebox.showinfo("Say Hello", "Hello World")
   
def reload():
    items.clear()
    c.delete(ALL)
    LeftFrame=Frame(c, width=250, height=500, borderwidth=1, background='white')
    LeftFrame.pack(fill="both",side="left")
    load_object(path,LeftFrame)
    c.create_window(1, 1,  window=LeftFrame)
    LeftFrame.update_idletasks()
    c.config(scrollregion=c.bbox("all"))

   
bouton_Reload = Button(fenetre,image=Rimgg,command=reload)
bouton_Reload.pack(side="left")
   
bouton_Play= Button(fenetre,image=Pimgg,command=reload)
bouton_Play.pack(side="left")

# On démarre la boucle Tkinter qui s'interompt quand on ferme la fenêtre
fenetre.mainloop()
