using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace Renommeur
{
    public partial class IHMNbFragments : Form
    {

        public static int nbFragmentsSaisis;

        public IHMNbFragments()
        {
            InitializeComponent();

            // Focus deja positionné sur le champ de saisie au chargement de la fenêtre
            nbFragmentsSaisie.Select();
        }


        private void btnDecouper_Click(object sender, EventArgs e)
        {
            if (nbFragmentsSaisie.Text.Equals(""))
            {
                // Affichage d'un message d'erreur
                MessageBox.Show("Certaines informations saisies sont incorrectes.", "Erreur de saisie",
                            MessageBoxButtons.OK, MessageBoxIcon.Error);

                return;
            }
            else
            {
                // Parsing de la saisie en int (avec suppression des éventuels espaces)
                nbFragmentsSaisis = Int32.Parse(nbFragmentsSaisie.Text.Replace(" ", String.Empty));
                System.Diagnostics.Debug.WriteLine("nbFragmentsSaisis : " + nbFragmentsSaisis);

                // Fermeture de la fenêtre de saisie
                this.Dispose();
                this.Close();
            }
        }


        private void btnAnnule_Click(object sender, EventArgs e)
        {
            // Réinitialisation de la valeur des fragments saisie souhaitée
            nbFragmentsSaisis = 0;

            // Fermeture de la fenêtre de saisie
            this.Dispose();
            this.Close();
        }
    }
}
