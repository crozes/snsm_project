# -*- coding: utf-8 -*
# On importe Tkinter
from tkinter import *
from PIL import ImageTk, Image
import json
import os,sys
path = "/home/pi/scene/"
files=os.listdir(path)
for file in files:
	print(file)
# On crée une fenêtre, racine de notre interface
fenetre = Tk()
#creation de la frame titre logo
LogoFrame = Frame(fenetre, width=500, height=50, borderwidth=1, background='orange')
LogoFrame.pack(fill="x")
#creation du canevas contenant le logo
canvas = Canvas(LogoFrame, width=50, height=50,background="orange")
img=Image.open("logosnsm.png")
img=img.resize((50,50), Image.ANTIALIAS)
imgg=ImageTk.PhotoImage(img)
canvas.create_image(25,25,image=imgg)

canvas.pack(side="left")
# On crée un label (ligne de texte) 
# Note : le premier paramètre passé au constructeur de Label est notre
# interface racine
champ_label = Label(LogoFrame, text="SimulBoat",background="orange",font="ArialBlack")

# On affiche le label dans la fenêtre
champ_label.pack(side="right",fill="x")
MainFrame=Frame(fenetre, width=500, height=500, borderwidth=1, background='black')
MainFrame.pack(fill="both")
LeftFrame=Frame(MainFrame, width=250, height=500, borderwidth=1, background='blue')
LeftFrame.pack(fill="both",side="left")
RightFrame=Frame(MainFrame, width=250, height=500, borderwidth=1, background='red')
RightFrame.pack(fill="both",side="right")
bouton_importer = Button(fenetre, text="Importer", command=fenetre.quit)
bouton_importer.pack()

liste = Listbox(LeftFrame)
liste.pack()
json_data=open("/home/pi/scene/jfjrjr.json",'r') 
data_dict =  json.load(json_data)
print(data_dict["nom"])
item="Nom: "+data_dict["nom"]+"Sens: "+data_dict["navigation"]+"Etat: "+data_dict["etat"]+"Date: "+data_dict["date"]
liste.insert(END,item)
# On démarre la boucle Tkinter qui s'interompt quand on ferme la fenêtre
fenetre.mainloop()
